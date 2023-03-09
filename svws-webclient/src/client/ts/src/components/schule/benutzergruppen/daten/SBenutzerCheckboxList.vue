<template>
	<!-- //TODO length klappt nicht.
	<div v-show="benutzerListe.length" class="w-full "></div> -->
	<div class="w-full ">
		<div v-if="spalteLinks" class="font-bond mb-1  h-6 center bg-green-300 rounded-lg flex justify-end place-items-center">
			<i-ri-arrow-right-circle-line class="center" />
		</div>

		<div v-if="!spalteLinks" class="font-bond mb-1 h-6 center bg-red-300 rounded-lg flex justify-start place-items-center">
			<svws-ui-icon v-if="!spalteLinks"> <i-ri-arrow-left-circle-line /> </svws-ui-icon>
		</div>

		<ul class=" divide-y divide-gray-100  ">
			<template v-for="benutzer in props.listBenutzer" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer="benutzer"
					v-model="aktiv" :spalte-links="spalteLinks"
					:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
					:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe" />
			</template>
		</ul>
		<slot />
	</div>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag, List } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";
	const props = defineProps<{
		listBenutzer: List<BenutzerListeEintrag>;
		title : string;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
	}>();

	const aktiv: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {
			// TODO
		}
	});

</script>
