<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col">
			<svws-ui-router-vertical-tab-bar :routes="children" :hidden="hidden" :model-value="child" @update:model-value="setChild">
				<router-view />
			</svws-ui-router-vertical-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { RouteRecordRaw, RouterView } from "vue-router";

	defineProps<{
		setChild: (value: RouteRecordRaw) => Promise<void>;
		child: RouteRecordRaw;
		children: RouteRecordRaw[];
		hidden: boolean[];
	}>();

</script>
