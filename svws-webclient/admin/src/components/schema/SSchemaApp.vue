<template>
	<div v-if="auswahl !== undefined" class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ auswahl.name }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							<template v-if="auswahl.revision < 0"> kein SVWS-Schema </template>
							<template v-else> Revision: {{ auswahl.revision }} </template>
						</svws-ui-badge>
					</h2>
					<span v-if="info !== undefined" class="svws-subline">{{ info.schulNr }} {{ info.schulform }} {{ info.bezeichnung }} | {{ info.strassenname }} {{ info.hausnummer }} {{ info.hausnummerZusatz ?? '' }} {{ info.ort }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-group-line />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchemaAppProps } from "./SSchemaAppProps";

	const props = defineProps<SchemaAppProps>();

	const info = computed(() => props.schuleInfo())

</script>
