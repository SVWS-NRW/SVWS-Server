<template>
	<div class="page page-flex-row pt-0">
		<div class="flex-none w-28 h-full overflow-y-auto pr-4">
			<svws-ui-table :clicked="lernabschnitt" @update:clicked="gotoLernabschnitt" :columns="[{key: 'schuljahresabschnitt', label: 'Abschnitt'}]" :items="lernabschnitte" clickable type="navigation" class="-mt-1">
				<template #cell="{ rowData: row }">
					<div>
						<span>
							{{ `${row.schuljahr}.${row.abschnitt}` }}&nbsp;{{ row.jahrgang ? `(${row.jahrgang})` : '' }}
						</span>
						<svws-ui-tooltip v-if="row.wechselNr !== 0">
							<span class="opacity-50 inline-block cursor-pointer top-0.5 relative">
								(alt)
							</span>
							<template #content>
								Daten vor dem {{ row.wechselNr }}. Wechsel
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
			</svws-ui-table>
		</div>
		<Teleport to=".svws-sub-nav-target" defer>
			<nav class="svws-ui-secondary-tabs">
				<svws-ui-tab-bar :tab-manager secondary :focus-switching-enabled :focus-help-visible />
			</nav>
		</Teleport>
		<div class="h-full overflow-y-auto pr-4">
			<router-view :key="$route.hash" />
		</div>
	</div>
</template>


<script setup lang="ts">

	import { useRegionSwitch } from "@ui";
	import type { SchuelerLernabschnitteProps } from "./SSchuelerLernabschnitteProps";

	const props = defineProps<SchuelerLernabschnitteProps>();

	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

</script>
