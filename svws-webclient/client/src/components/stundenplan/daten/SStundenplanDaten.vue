<template>
	<div class="page--content">
		<s-card-stundenplan-daten :stundenplan-manager="stundenplanManager" :patch="patch" :list-jahrgaenge="listJahrgaenge" :add-jahrgang="addJahrgang" :remove-jahrgang="removeJahrgang" />
		<s-card-stundenplan-pausenzeiten :stundenplan-manager="stundenplanManager" :patch-pausenzeit="patchPausenzeit" :add-pausenzeit="addPausenzeit" :remove-pausenzeiten="removePausenzeiten" :list-pausenzeiten="pausenzeiten" :import-pausenzeiten="importPausenzeiten" :goto-katalog="gotoKatalog" />
		<s-card-stundenplan-raeume :stundenplan-manager="stundenplanManager" :patch-raum="patchRaum" :add-raum="addRaum" :remove-raeume="removeRaeume" :import-raeume="importRaeume" :list-raeume="raeume" :goto-katalog="gotoKatalog" />
		<s-card-stundenplan-aufsichtsbereiche :stundenplan-manager="stundenplanManager" :patch-aufsichtsbereich="patchAufsichtsbereich" :add-aufsichtsbereich="addAufsichtsbereich" :remove-aufsichtsbereiche="removeAufsichtsbereiche" :list-aufsichtsbereiche="aufsichtsbereiche" :import-aufsichtsbereiche="importAufsichtsbereiche" :goto-katalog="gotoKatalog" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { Raum, StundenplanAufsichtsbereich, StundenplanPausenzeit } from "@core";
	import { ArrayList } from "@core";
	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";

	const props = defineProps<StundenplanDatenProps>();

	const raeume = computed(() => {
		const moeglich = new ArrayList<Raum>();
		for (const e of props.listRaeume())
			if (!props.stundenplanManager().raumExistsByKuerzel(e.kuerzel))
				moeglich.add(e);
		return moeglich;
	})

	const pausenzeiten = computed(() => {
		const moeglich = new ArrayList<StundenplanPausenzeit>();
		for (const e of props.listPausenzeiten())
			if (!props.stundenplanManager().pausenzeitExistsByWochentagAndBeginnAndEnde(e.wochentag, e.beginn, e.ende))
				moeglich.add(e);
		return moeglich;
	})

	const aufsichtsbereiche = computed(() => {
		const moeglich = new ArrayList<StundenplanAufsichtsbereich>();
		for (const e of props.listAufsichtsbereiche())
			if (!props.stundenplanManager().aufsichtsbereichExistsByKuerzel(e.kuerzel))
				moeglich.add(e);
		return moeglich;
	})



</script>