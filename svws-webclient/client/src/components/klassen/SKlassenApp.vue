<template>
	<template v-if="klassenListeManager().hasDaten() || props.gruppenprozesseEnabled">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<template v-if="props.gruppenprozesseEnabled">
						<h2 class="svws-headline">
							Gruppenprozesse
						</h2>
						<span class="svws-subline">{{ selectedKlassen }}</span>
					</template>
					<template v-else>
						<h2 class="svws-headline">
							<span>{{ klassenListeManager().daten().kuerzel ? 'Klasse ' + klassenListeManager().daten().kuerzel : '—' }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID:
								{{ klassenListeManager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</template>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="props.gruppenprozesseEnabled ? tabsGruppenprozesse : tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-team-line" />
	</div>
</template>

<script setup lang="ts">

	import type { KlassenAppProps } from "./SKlassenAppProps";
	import { computed } from "vue";

	const props = defineProps<KlassenAppProps>();

	const selectedKlassen = computed<string>(() => {
		const liste = props.klassenListeManager().liste.auswahlSorted();
		let str = "";
		for (const kl of liste)
			str += (str.length > 0 ? ", " : "") + kl.kuerzel;
		return str;
	});

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (props.klassenListeManager().hasDaten()) {
			for (const id of props.klassenListeManager().daten().klassenLeitungen) {
				const lehrer = props.klassenListeManager().lehrer.get(id);
				if (lehrer !== null) {
					if (s.length)
						s += `, ${lehrer.kuerzel}`;
					else s = lehrer.kuerzel;
				}
			}
		}
		return s;
	});

</script>
