<template>
	<div v-if="true" class="flex w-full flex-col h-full">
		<svws-ui-header>
			<div class="flex items-center">
				<div>
					<span class="inline-block mr-3"> {{ daten.bezeichnungStundenplan }} </span>
					<svws-ui-badge type="light" title="ID" class="font-mono">
						<i-ri-fingerprint-line />
						{{ daten.id }}
					</svws-ui-badge>
					<br>
					<span class="opacity-50"> </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-calendar-event-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import { StundenplanAppProps } from "./SStundenplanAppProps";

	const props = defineProps<StundenplanAppProps>();

</script>
