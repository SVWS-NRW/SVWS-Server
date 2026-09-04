<template>
	<svws-ui-header>
		<div class="flex flex-col w-full h-full overflow-hidden">
			<header class="svws-ui-header">
				<div class="svws-ui-header--title">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ schuleState.stammdaten.bezeichnung1 }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								Schulnummer: {{ schuleState.stammdaten.schulNr }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ `${schuleState.stammdaten.bezeichnung2 ?? ''} ${schuleState.stammdaten.bezeichnung3 ?? ''}` }}</span>
					</div>
				</div>
				<div class="svws-ui-header--actions">
					<svws-ui-modal-hilfe> <hilfe-schule-stammdaten /> </svws-ui-modal-hilfe>
				</div>
			</header>
		</div>
	</svws-ui-header>
	<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
		<router-view />
	</svws-ui-tab-bar>
</template>

<script setup lang="ts">

	import { useSchuleState } from "@ui/states/SchuleState";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";
	import type { SchuleStammdatenProps } from "~/components/schule/stammdaten/SchuleStammdatenProps";

	defineProps<SchuleStammdatenProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const schuleState = useSchuleState();


</script>
