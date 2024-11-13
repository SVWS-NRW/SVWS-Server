<template>
	<template v-if="(kursListeManager().hasDaten() && (activeViewType === ViewType.DEFAULT)) || (activeViewType !== ViewType.DEFAULT)">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<template v-if="activeViewType === ViewType.DEFAULT">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ kursListeManager().daten().kuerzel }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								ID:
								{{ kursListeManager().daten().id }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">
							{{ lehrerkuerzel }}
						</span>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.HINZUFUEGEN">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Anlegen eines neuen Kurses...</h2>
					</div>
				</template>
				<template v-else-if="activeViewType === ViewType.GRUPPENPROZESSE">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">Gruppenprozesse</h2>
						<span class="svws-subline">{{ kurseSubline }}</span>
					</div>
				</template>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-tab-bar :tab-manager enable-focus-switching>
			<router-view />
		</svws-ui-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-presentation-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { KurseAppProps } from "./SKurseAppProps";
	import { ViewType } from "@ui";

	const props = defineProps<KurseAppProps>();

	const lehrerkuerzel = computed<string>(() => {
		let s = '';
		if (props.kursListeManager().hasDaten()) {
			const idLehrer = props.kursListeManager().daten().lehrer;
			const lehrer = idLehrer === null ? null : props.kursListeManager().lehrer.get(idLehrer);
			s = (lehrer === null) ? " " : lehrer.kuerzel;
			// TODO Zusatzkräfte
			// for (const idZusatzkraft of props.kursListeManager().daten().zusatzkraefte) {
			// 	const zusatzkraft = props.kursListeManager().lehrer.get(idZusatzkraft);
			// 	if (zusatzkraft !== null) {
			// 		if (s.length)
			// 			s += `, ${lehrer.kuerzel}`;
			// 		else s = lehrer.kuerzel;
			// 	}
			// }
		}
		return s;
	});

	const kurseSubline = computed(() => {
		const auswahlKurseList = props.kursListeManager().liste.auswahlSorted();
		if (auswahlKurseList.size() > 5)
			return `${auswahlKurseList.size()} Kurse ausgewählt`;
		return [...auswahlKurseList].map(k => k.kuerzel).join(', ');
	})

</script>
