<template>
	<div class="text-headline-md">Benutzer</div>
	<div class="flex flex-row min-w-fit gap-4 overflow-y-hidden">
		<s-benutzer-checkbox-list title="Einfügen" :spalte-links="true"
			:benutzer-list="() => benutzerNichtInBenutzergruppe"
			:add-benutzer-to-benutzergruppe
			:remove-benutzer-from-benutzergruppe
			:goto-benutzer
			:aktueller-benutzer />
		<s-benutzer-checkbox-list title="Entfernen" :spalte-links="false"
			:benutzer-list="benutzerInBenutzergruppe"
			:add-benutzer-to-benutzergruppe
			:remove-benutzer-from-benutzergruppe
			:goto-benutzer
			:aktueller-benutzer />
	</div>
</template>

<script setup lang="ts">

	import type { BenutzerDaten, BenutzerListeEintrag, List } from "@core";
	import { ArrayList } from "@core";
	import { computed } from "vue";

	const props = defineProps<{
		alleBenutzer: () => List<BenutzerListeEintrag> ;
		benutzerInBenutzergruppe: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (idBenutzer: number) => Promise<void>;
		aktuellerBenutzer: BenutzerDaten;
	}>();

	const benutzerNichtInBenutzergruppe = computed<List<BenutzerListeEintrag>>(() => {
		const benutzerInBenutzergruppeArr = [...props.benutzerInBenutzergruppe()];
		const benutzerNichtInBenutzergruppe = new ArrayList<BenutzerListeEintrag>();
		for (const benutzer of props.alleBenutzer()) {
			if (benutzerInBenutzergruppeArr.every(e => e.id !== benutzer.id)) {
				benutzerNichtInBenutzergruppe.add(benutzer);
			}
		}
		return benutzerNichtInBenutzergruppe;
	});

</script>
