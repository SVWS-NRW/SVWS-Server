<template>
	<li>
		<label class=" cursor-pointer p-2 hover:bg-sky-300 flex justify-between items-center rounded-lg"
			@mouseover="showIcon=true" @mouseout="showIcon=false"
			@click.prevent="add()"
			:class="icon ? 'hover:bg-green-300' : 'hover:bg-red-300'">
			<span> {{ benutzer.id }}-{{ benutzer.name }} </span>
			<!-- <input type="checkbox" v-model="model" class="ml-4"> -->
			<svws-ui-icon v-if="icon" v-show="showIcon"> <i-ri-user-add-line /> </svws-ui-icon>
			<svws-ui-icon v-if="!icon" v-show="showIcon"> <i-ri-user-unfollow-line /> </svws-ui-icon>
		</label>
	</li>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { ref } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
		benutzer: BenutzerListeEintrag;
		icon : boolean;
	}>();

	const showIcon = ref(false);

	function add() {
		props.icon
			? props.data.addBenutzergruppeBenutzer(props.benutzer)
			: props.data.removeBenutzergruppeBenutzer(props.benutzer)
	}

</script>
