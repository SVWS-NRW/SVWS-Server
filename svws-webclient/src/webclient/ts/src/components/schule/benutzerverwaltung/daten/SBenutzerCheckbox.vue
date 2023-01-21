<template>
	<li>
		<label class=" cursor-pointer p-2 hover:bg-sky-300 flex justify-between items-center rounded-lg"
			@mouseover="showIcon=true" @mouseout="showIcon=false"
			@click.prevent="add()"
			:class="icon ? 'hover:bg-green-300' : 'hover:bg-red-300'">
			<span>{{ object.id }}-{{ object.name }}</span>
			<!-- <input type="checkbox" v-model="model" class="ml-4"> -->
			<svws-ui-icon v-if="icon" v-show="showIcon">
				<i-ri-user-add-line />
			</svws-ui-icon>
			<svws-ui-icon v-if="!icon" v-show="showIcon">
				<i-ri-user-unfollow-line />
			</svws-ui-icon>

		</label>
	</li>
</template>

<script setup lang="ts">
	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ref } from "vue";
	import { App } from "~/apps/BaseApp";

	const showIcon = ref(false);
	const {
		object,
		icon

	} = defineProps<{
		object: { type: BenutzerListeEintrag, required: true }
		icon:Boolean
	}>();

	function add(){
		icon ? App.apps.benutzergruppe.dataBenutzergruppe.addBenutzergruppeBenutzer(object)
		: App.apps.benutzergruppe.dataBenutzergruppe.removeBenutzergruppeBenutzer(object)
	}



</script>

<style>

</style>