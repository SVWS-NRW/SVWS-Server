<template>
	<!-- //TODO length klappt nicht.
	<div v-show="benutzerListe.length" class="w-full "></div> -->
	<div class="w-full">
		<div class="flex justify-between items-center font-bold text-button mb-2">
			<template v-if="spalteLinks">
				<span>Verfügbare Benutzer</span>
			</template>
			<template v-else>
				<span>Aktiv in dieser Gruppe</span>
			</template>
		</div>
		<ul class="flex flex-col gap-0.5">
			<template v-for="benutzer in listBenutzer()" :key="benutzer.id">
				<s-benutzer-checkbox :benutzer="benutzer"
					v-model="aktiv" :spalte-links="spalteLinks"
					:add-benutzer-to-benutzergruppe="addBenutzerToBenutzergruppe"
					:remove-benutzer-from-benutzergruppe="removeBenutzerFromBenutzergruppe"
					:go-to-benutzer="goToBenutzer" />
			</template>
		</ul>
		<slot />
	</div>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag, List } from "@svws-nrw/svws-core";
	import { computed, WritableComputedRef } from "vue";
	const props = defineProps<{
		listBenutzer: () => List<BenutzerListeEintrag>;
		title : string;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
	}>();

	const aktiv: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {
			// TODO
		}
	});

</script>
