<template>
	<div v-show="benutzerListe?.length" class="w-full ">
		<div v-if="spalteLinks" class="font-bond mb-1  h-6 center bg-green-300 rounded-lg flex justify-end place-items-center">
			<i-ri-arrow-right-circle-line class="center" />
		</div>

		<div v-if="!spalteLinks" class="font-bond mb-1 h-6 center bg-red-300 rounded-lg flex justify-start place-items-center">
			<svws-ui-icon v-if="!spalteLinks"> <i-ri-arrow-left-circle-line /> </svws-ui-icon>
		</div>

		<ul class=" divide-y divide-gray-100  ">
			<template v-for="benutzer in benutzerListe" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer="benutzer" v-model="aktiv" :spalte-links="spalteLinks" :data="data" />
			</template>
		</ul>
		<slot />
	</div>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, WritableComputedRef } from "vue";
	import { DataBenutzergruppe } from "~/apps/schule/benutzerverwaltung/DataBenutzergruppe";

	const props = defineProps<{
		data: DataBenutzergruppe;
		benutzerListe : BenutzerListeEintrag[];
		title : string;
		spalteLinks : boolean;
	}>();

	const aktiv: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {
			// TODO
		}
	});

</script>
