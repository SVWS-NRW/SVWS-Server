<template>
	<div class="text-headline-md">Benutzer</div>
	<div class="flex flex-row min-w-fit gap-4 overflow-y-hidden">
		<s-benutzer-checkbox-list title="Einfügen" :spalte-links="true"
			:benutzer-list="() => benutzerNichtInBenutzergruppe"
			:add-benutzer-to-benutzergruppe
			:remove-benutzer-from-benutzergruppe
			:goto-benutzer />
		<s-benutzer-checkbox-list title="Entfernen" :spalte-links="false"
			:benutzer-list="benutzerInBenutzergruppe"
			:add-benutzer-to-benutzergruppe
			:remove-benutzer-from-benutzergruppe
			:goto-benutzer />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";

	const props = defineProps<{
		alleBenutzer: () => List<BenutzerListeEintrag> ;
		benutzerInBenutzergruppe: () => List<BenutzerListeEintrag>;
		addBenutzerToBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		removeBenutzerFromBenutzergruppe: (benutzer: BenutzerListeEintrag) => Promise<void>;
		gotoBenutzer: (idBenutzer: number) => Promise<void>;
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
