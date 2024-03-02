<template>
	<div class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ schulname1 }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							Schulnummer: {{ schule().schulNr }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ schulname2 + " " + schulname3 }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="[ tab ]" :hidden="[ false ]" :model-value="tab">
			<s-schule-stammdaten :schule="schule" :patch="patch" :benutzer-kompetenzen="benutzerKompetenzen" :benutzer-ist-admin="benutzerIstAdmin" />
			<div class="app--content--placeholder flex flex-col items-center">
				<i-ri-school-line />
			</div>
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchuleAppProps } from "./SSchuleAppProps";
	import { type AuswahlChildData } from "../AuswahlChildData";

	const props = defineProps<SchuleAppProps>();

	const tab: AuswahlChildData = { name: 'stammdaten', text: 'Stammdaten' };

	const schulname1 = computed<string>(() => {
		const name = props.schule()?.bezeichnung1;
		return name ? name : "";
	});

	const schulname2 = computed<string>(() => {
		const name = props.schule()?.bezeichnung2;
		return name ? name : "";
	});

	const schulname3 = computed<string>(() => {
		const name = props.schule()?.bezeichnung3;
		return name ? name : "";
	});

</script>
