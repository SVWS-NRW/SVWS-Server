<template>
	<div v-if="auswahl !== undefined" class="page--flex">
		<header class="svws-ui-header max-w-[140rem]">
			<div class="svws-ui-header--title gap-x-8 lg:gap-x-16 w-full">
				<div class="svws-headline-wrapper flex-[2]">
					<h2 class="svws-headline">
						<span>{{ auswahl.name }}</span>
					</h2>
					<span class="svws-subline">
						<span v-if="auswahl.revision < 0" class="opacity-50"> Kein SVWS-Schema </span>
						<span v-else> Revision: {{ auswahl.revision }} </span>
					</span>
				</div>
				<div v-if="info !== undefined" class="flex-1 flex flex-col bg-light py-2 px-4 rounded-lg text-base -mr-3">
					<span class="text-headline-md">{{ info.bezeichnung.split('\n')[0] }}</span>
					<span class="opacity-50 line-clamp-1" :title="`${info.bezeichnung} (${ info.strassenname } ${ info.hausnummer } ${ info.hausnummerZusatz ?? '' } ${ info.ort })`">{{ info.bezeichnung.split('\n').slice(1).join('\n') }} ({{ info.strassenname }} {{ info.hausnummer }} {{ info.hausnummerZusatz ?? '' }} {{ info.ort }})</span>
					<span class="opacity-50">{{ info.schulNr }} {{ info.schulform }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-stack-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchemaAppProps } from "./SSchemaAppProps";

	const props = defineProps<SchemaAppProps>();

	const info = computed(() => props.schuleInfo())

</script>
