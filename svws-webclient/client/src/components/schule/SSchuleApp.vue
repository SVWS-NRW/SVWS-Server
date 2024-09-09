<template>
	<div class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ schule().bezeichnung1 }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							Schulnummer: {{ schule().schulNr }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ schule().bezeichnung2 ?? "" + " " + schule().bezeichnung3 ?? "" }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="[ tab ]" :hidden="[ false ]" :model-value="tab">
			<s-schule-stammdaten :schule :patch :smpt-server-konfiguration :patch-s-m-t-p-server-konfiguration :benutzer-kompetenzen :benutzer-ist-admin />
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleAppProps } from "./SSchuleAppProps";
	import type { AuswahlChildData } from "../AuswahlChildData";

	const props = defineProps<SchuleAppProps>();

	const tab: AuswahlChildData = { name: 'stammdaten', text: 'Stammdaten' };

</script>
