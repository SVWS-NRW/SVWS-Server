<template>
	<div v-if="visible" class="app-container">
		<s-card-schueler-halbjahr :data="data!" @patch="patch" :map-lehrer="mapLehrer" :map-jahrgaenge="mapJahrgaenge" :map-klassen="mapKlassen"
			:schule="schule" :map-foerderschwerpunkte="mapFoerderschwerpunkte" />
		<s-card-schueler-zeugnis-abschluss :data="data!" @patch="patch" @patch-bemerkungen="patchBemerkungen" :map-klassen="mapKlassen" />
		<s-card-schueler-abschluss-berechnung :data="data!" @patch="patch" />
		<s-card-schueler-fehlzeiten :data="data!" @patch="patch" />
	</div>
</template>

<script setup lang="ts">

	import { FoerderschwerpunktEintrag, JahrgangsListeEintrag, KlassenListeEintrag, LehrerListeEintrag,
		SchuelerLernabschnittBemerkungen, SchuelerLernabschnittsdaten, SchuleStammdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		schule: SchuleStammdaten;
		data: SchuelerLernabschnittsdaten | undefined;
		mapLehrer: Map<number, LehrerListeEintrag>;
		mapJahrgaenge: Map<number, JahrgangsListeEintrag>;
		mapKlassen: Map<number, KlassenListeEintrag>;
		mapFoerderschwerpunkte: Map<number, FoerderschwerpunktEintrag>;
		patch: (data : Partial<SchuelerLernabschnittsdaten>) => Promise<void>;
		patchBemerkungen: (data : Partial<SchuelerLernabschnittBemerkungen>) => Promise<void>;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (props.data !== undefined);
	});

</script>
