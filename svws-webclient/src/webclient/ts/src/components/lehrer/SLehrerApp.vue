<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-20 mr-4">
					<svws-ui-avatar :src="'data:image/png;base64, ' + stammdaten?.foto ?? undefined"
						:alt="(stammdaten !== undefined) && (stammdaten.foto !== null) ? 'Foto ' + stammdaten.vorname + ' ' + stammdaten.nachname : ''" upload capture />
				</div>
				<div v-if="stammdaten !== undefined">
					<span class="inline-block mr-3">{{ stammdaten.titel }} {{ stammdaten.vorname }} {{ stammdaten.nachname }}</span>
					<svws-ui-badge type="light" title="ID" class="font-mono">
						<i-ri-fingerprint-line />
						{{ stammdaten.id }}
					</svws-ui-badge>
					<br>
					<span class="opacity-50">{{ stammdaten.kuerzel }}</span>
				</div>
				<div v-else>
					<span class="inline-block mr-3"> --- </span>
					<br>
					<span class="opacity-50"> --- </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line />
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { LehrerAppProps } from "./SLehrerAppProps";

	const props = defineProps<LehrerAppProps>();

	const visible: ComputedRef<boolean> = computed(() => props.stammdaten !== undefined);

</script>
