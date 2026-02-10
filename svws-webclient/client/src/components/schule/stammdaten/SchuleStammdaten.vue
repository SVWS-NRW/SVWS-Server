<template>
	<svws-ui-header>
		<div class="flex flex-col w-full h-full overflow-hidden">
			<header class="svws-ui-header">
				<div class="svws-ui-header--title">
					<div class="svws-headline-wrapper">
						<h2 class="svws-headline">
							<span>{{ schule().bezeichnung1 }}</span>
							<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
								Schulnummer: {{ schule().schulNr }}
							</svws-ui-badge>
						</h2>
						<span class="svws-subline">{{ `${schule().bezeichnung2 ?? ''} ${schule().bezeichnung3 ?? ''}` }}</span>
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

	import { useRegionSwitch } from "@ui";
	import type { SchuleStammdatenProps } from "~/components/schule/stammdaten/SchuleStammdatenProps";

	defineProps<SchuleStammdatenProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();


</script>
