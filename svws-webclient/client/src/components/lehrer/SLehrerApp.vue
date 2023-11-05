<template>
	<template v-if="lehrerListeManager().hasDaten()">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<svws-ui-avatar :src="'data:image/png;base64, ' + lehrerListeManager().daten().foto ?? undefined"
					:alt="(lehrerListeManager().daten() !== undefined) && (lehrerListeManager().daten().foto !== null) ? 'Foto ' + lehrerListeManager().daten().vorname + ' ' + lehrerListeManager().daten().nachname : ''" upload capture />
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						{{ lehrerListeManager().daten().titel }} {{ lehrerListeManager().daten().vorname }} {{ lehrerListeManager().daten().nachname }}
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID:
							{{ lehrerListeManager().daten().id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ lehrerListeManager().daten().kuerzel }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-briefcase-line />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerAppProps } from "./SLehrerAppProps";

	const props = defineProps<LehrerAppProps>();

</script>
