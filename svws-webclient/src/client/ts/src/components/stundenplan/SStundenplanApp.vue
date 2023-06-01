<template>
	<div v-if="true" class="page--flex">
		<svws-ui-header>
			<div class="flex items-center">
				<div>
					<span class="inline-block mr-3"> {{ data().bezeichnungStundenplan }} </span>
					<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
						ID:
						{{ data().id }}
					</svws-ui-badge>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-calendar-event-line />
	</div>
</template>

<script setup lang="ts">

	import type { StundenplanAppProps } from "./SStundenplanAppProps";

	const props = defineProps<StundenplanAppProps>();

</script>
