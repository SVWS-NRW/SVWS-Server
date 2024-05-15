<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-input-wrapper>
				<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
				<svws-ui-checkbox v-model="istStatistikrelevant"> Ist statistikrelevant </svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({kuerzel})" type="text" required />
			<svws-ui-select title="Personal-Typ" v-model="inputPersonalTyp" :items="PersonalTyp.values()" :item-text="i => i.bezeichnung" required />
			<svws-ui-text-input placeholder="Nachname" :model-value="data.nachname" @change="nachname => patch({nachname})" type="text" required />
			<svws-ui-text-input placeholder="Rufname" :model-value="data.vorname" @change="vorname => patch({vorname})" type="text" required />
			<svws-ui-spacing />
			<svws-ui-select title="Geschlecht" v-model="inputGeschlecht" :items="Geschlecht.values()" :item-text="i=>i.text" required />
			<svws-ui-text-input placeholder="Geburtsdatum" :model-value="data.geburtsdatum" @change="geburtsdatum => geburtsdatum && patch({geburtsdatum})" type="date" required />
			<svws-ui-select title="Staatsangehörigkeit" v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
				:item-text="i => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
				:item-filter="staatsangehoerigkeitKatalogEintragFilter" required autocomplete />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Akadademischer Grad" :model-value="data.titel" @change="titel => patch({titel})" type="text" />
			<svws-ui-text-input placeholder="Amtsbezeichnung" :model-value="data.amtsbezeichnung" @change="amtsbezeichnung => patch({amtsbezeichnung})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { LehrerListeManager, LehrerStammdaten } from "@core";
	import { computed } from "vue";
	import { DeveloperNotificationException, Geschlecht, Nationalitaeten, PersonalTyp } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	}>();

	const data = computed<LehrerStammdaten>(() => props.lehrerListeManager().daten());

	const inputGeschlecht = computed<Geschlecht>({
		get: () => Geschlecht.fromValue(data.value.geschlecht) || Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id })
	});

	const inputPersonalTyp = computed<PersonalTyp>({
		get: () => PersonalTyp.values().find(i => i.kuerzel === data.value.personalTyp) || PersonalTyp.SONSTIGE,
		set: (value) => void props.patch({ personalTyp: value.kuerzel })
	});

	const inputStaatsangehoerigkeit = computed<Nationalitaeten>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const istSichtbar = computed<boolean>({
		get: () => true,
		set: (value) => {throw new DeveloperNotificationException("Sichtbarkeit ist noch nicht implementiert")}//doPatch({ istSichtbar: value })
	});

	const istStatistikrelevant = computed<boolean>({
		get: () => true,
		set: (value) => {throw new DeveloperNotificationException("Sichtbarkeit ist noch nicht implementiert")}//doPatch({ istStatistikrelevant: value })
	});


</script>
