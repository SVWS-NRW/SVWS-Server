<template>
	<!-- //TODO length klappt nicht.
	<div v-show="benutzerListe.length" class="w-full "></div> -->
	<div class="w-full">
		<div class="flex justify-between items-center font-bold text-button border border-black/25 py-[0.1rem] px-2 h-11" :class="{'border-l-0': !spalteLinks}">
			<template v-if="spalteLinks">
				<span>Verfügbare Benutzer</span>
				<span>ID</span>
			</template>
			<template v-else>
				<span>Aktiv in dieser Gruppe</span>
				<span>ID</span>
			</template>
		</div>
		<ul class="flex flex-col">
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

	import type { BenutzerListeEintrag, List } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";
	const props = defineProps<{
		listBenutzer: () => List<BenutzerListeEintrag>;
		title : string;
		spalteLinks : boolean;
		addBenutzerToBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe : (benutzer: BenutzerListeEintrag) => Promise<void>;
		goToBenutzer: (b_id: number) => Promise<void>;
		listBenutzerInGruppe: () => List<BenutzerListeEintrag>;
	}>();

	const aktiv: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {
			// TODO
		}
	});

</script>
