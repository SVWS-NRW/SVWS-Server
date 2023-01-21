<template>
	<section v-show="benutzer?.length" class="w-full ">
		<div class="font-bond mb-1  center bg-gray-200 rounded-lg flex justify-between  items-center">
			<h1 class="ml-2 font-bold">
				{{ title }}
				
			</h1>
			<svws-ui-icon v-if="icon">
				<i-ri-user-add-line />
			</svws-ui-icon>
			<svws-ui-icon v-if="!icon">
				<i-ri-user-unfollow-line />
			</svws-ui-icon>
		</div>
		<ul class=" divide-y divide-gray-100 overflow-y-auto h-fit">
			<s-benutzer-checkbox v-for="item in benutzer"
				:key="item?.id"
				:object="item"
				v-model="aktiv"
				:icon="icon" />
		</ul>
		<slot />
	</section>
</template>

<script setup lang="ts">
	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";

	const props=defineProps({
		benutzer : Array ,
		title: String,
		icon:Boolean
	})

	const aktiv: WritableComputedRef<boolean> = computed({
		get(): boolean {
			return true;
		},
		set(value: boolean) {
			console.log("Hallo");
		}
	});

</script>

<style>

</style>