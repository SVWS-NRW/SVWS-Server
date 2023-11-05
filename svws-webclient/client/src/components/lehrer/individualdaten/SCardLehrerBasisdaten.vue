<template>
	<svws-ui-content-card title="Allgemein">
		<template #actions>
			<svws-ui-checkbox v-model="istSichtbar"> Ist sichtbar </svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" :model-value="data.kuerzel" @change="kuerzel => patch({kuerzel})" type="text" required />
			<svws-ui-select title="Personal-Typ" v-model="inputPersonalTyp" :items="PersonalTyp.values()" :item-text="(i: PersonalTyp) => i.bezeichnung" required />
			<svws-ui-text-input placeholder="Nachname" :model-value="data.nachname" @change="nachname => patch({nachname})" type="text" required />
			<svws-ui-text-input placeholder="Rufname" :model-value="data.vorname" @change="vorname => patch({vorname})" type="text" required />
			<svws-ui-spacing />
			<svws-ui-select title="Geschlecht" v-model="inputGeschlecht" :items="Geschlecht.values()" :item-text="i=>i.text" required />
			<svws-ui-text-input placeholder="Geburtsdatum" :model-value="data.geburtsdatum" @change="geburtsdatum => patch({geburtsdatum})" type="date" required />
			<svws-ui-select title="Staatsangehörigkeit" v-model="inputStaatsangehoerigkeit" :items="Nationalitaeten.values()"
				:item-text="(i: Nationalitaeten) => i.daten.staatsangehoerigkeit" :item-sort="staatsangehoerigkeitKatalogEintragSort"
				:item-filter="staatsangehoerigkeitKatalogEintragFilter" required autocomplete />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Akad.Grad" :model-value="data.titel" @change="titel => patch({titel})" type="text" />
			<svws-ui-text-input placeholder="Amtsbezeichnung" :model-value="data.amtsbezeichnung" @change="amtsbezeichnung => patch({amtsbezeichnung})" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { WritableComputedRef } from "vue";
	import type { LehrerListeManager, LehrerStammdaten} from "@core";
	import { computed } from "vue";
	import { DeveloperNotificationException, Geschlecht, Nationalitaeten, PersonalTyp } from "@core";
	import { staatsangehoerigkeitKatalogEintragFilter, staatsangehoerigkeitKatalogEintragSort } from "~/utils/helfer";

	const props = defineProps<{
		lehrerListeManager: () => LehrerListeManager;
		patch: (data : Partial<LehrerStammdaten>) => Promise<void>;
	}>();

	const data = computed<LehrerStammdaten>(() => props.lehrerListeManager().daten());

	const inputGeschlecht: WritableComputedRef<Geschlecht> = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) || Geschlecht.X,
		set: (value) => void props.patch({ geschlecht: value.id })
	});

	const inputPersonalTyp: WritableComputedRef<PersonalTyp> = computed({
		get: () => PersonalTyp.values().find(i => i.kuerzel === data.value.personalTyp) || PersonalTyp.SONSTIGE,
		set: (value) => void props.patch({ personalTyp: value.kuerzel })
	});

	const inputStaatsangehoerigkeit: WritableComputedRef<Nationalitaeten> = computed({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID) || Nationalitaeten.DEU,
		set: (value) => void props.patch({ staatsangehoerigkeitID: value.daten.iso3 })
	});

	const istSichtbar: WritableComputedRef<boolean> = computed({
		get: () => true,
		set: (value) => {throw new DeveloperNotificationException("Sichtbarkeit ist noch nicht implementiert")}//doPatch({ istSichtbar: value })
	});


</script>
