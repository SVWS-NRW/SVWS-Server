<template>
	<li class="flex flex-row place-items-center justify-between ">
		<svws-ui-icon title="Zeige Benutzer"> <i-ri-external-link-fill @click="doRoute()" class="flex-initial cursor-pointer align-botton ml-3 " /> </svws-ui-icon>
		<label class=" cursor-pointer p-2 hover:bg-sky-300 flex flex-1 rounded-lg"
			@mouseover="showIcon=true" @mouseout="showIcon=false"
			@click.prevent="add()"
			:class="spalteLinks ? 'hover:bg-red-300' : 'hover:bg-green-300'">
			<svws-ui-icon title="Füge Benutzer hinzu" v-if="!spalteLinks" v-show="showIcon"> <i-ri-arrow-left-circle-line /> </svws-ui-icon>
			<span> {{ benutzer.id }}-{{ benutzer.name }} </span>
			<svws-ui-icon title="Entferne Benutzer" v-if="spalteLinks" v-show="showIcon"> <i-ri-arrow-right-circle-line /> </svws-ui-icon>

		</label>
	</li>
</template>

<script setup lang="ts">

	import { BenutzergruppeDaten, BenutzerListeEintrag } from "@svws-nrw/svws-core";
	import { ref } from "vue";
	import { router } from "~/router/RouteManager";
	import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";

	const props = defineProps<{
		benutzer: BenutzerListeEintrag;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
	}>();

	const showIcon = ref(false);

	async function add() {
		props.spalteLinks
			? await props.removeBenutzerFromBenutzergruppe(props.benutzer)
			: await props.addBenutzerToBenutzergruppe(props.benutzer)
	}

	function doRoute() {
		void router.push({ name: routeSchuleBenutzerDaten.name, params: { id: props.benutzer.id } });
	}

</script>
