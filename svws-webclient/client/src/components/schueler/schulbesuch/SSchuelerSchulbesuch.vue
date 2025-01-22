<template>
	<div class="page--content">
		<svws-ui-content-card title="Vor der Aufnahme besucht">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" v-autofocus placeholder="Name der Schule" :model-value="data.vorigeSchulnummer" @change="vorigeSchulnummer=>patch({ vorigeSchulnummer })" type="text" />
				<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="data.vorigeAllgHerkunft" @change="vorigeAllgHerkunft=>patch({ vorigeAllgHerkunft })" type="text" />
				<svws-ui-text-input placeholder="Entlassen am" :model-value="data.vorigeEntlassdatum" @change="vorigeEntlassdatum=>patch({ vorigeEntlassdatum })" type="date" />
				<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.vorigeEntlassjahrgang" @change="vorigeEntlassjahrgang=>patch({ vorigeEntlassjahrgang })" type="text" />
				<svws-ui-text-input placeholder="Bemerkung" :model-value="data.vorigeBemerkung" @change="vorigeBemerkung=>patch({ vorigeBemerkung })" type="text" span="full" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Entlassgrund" :model-value="data.vorigeEntlassgrundID?.toString()" @change="vorigeEntlassgrundID => vorigeEntlassgrundID && patch({ vorigeEntlassgrundID: parseInt(vorigeEntlassgrundID) })" type="text" />
				<svws-ui-text-input placeholder="höchster allg.-bild. Abschluss" :model-value="data.vorigeAbschlussartID" @change="vorigeAbschlussartID=>patch({ vorigeAbschlussartID })" type="text" />
				<svws-ui-select title="Versetzung" v-model="vorigeArtLetzteVersetzung" :items="herkunftsarten" :item-text="(h: Herkunftsarten) => getBezeichnung(h) + ' (' + h.daten.kuerzel + ')'" :statistics="showstatistic" class="col-span-full" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Entlassung am" :model-value="data.entlassungDatum" @change="entlassungDatum=>patch({ entlassungDatum })" type="date" />
				<svws-ui-text-input placeholder="Entlassjahrgang" :model-value="data.entlassungJahrgang" @change="entlassungJahrgang=>patch({ entlassungJahrgang })" type="text" />
				<svws-ui-text-input placeholder="Bemerkung oder Entlassgrund" :model-value="data.entlassungGrundID?.toString()" @change="entlassungGrundID=> entlassungGrundID && patch({ entlassungGrundID: parseInt(entlassungGrundID)})" span="full" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Art des Abschlusses" :model-value="data.entlassungAbschlussartID" @change="entlassungAbschlussartID=>patch({ entlassungAbschlussartID })" span="full" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox :model-value="data.aufnehmdendBestaetigt === true" :indeterminate="data.aufnehmdendBestaetigt === null" @update:model-value="aufnehmdendBestaetigt => patch({ aufnehmdendBestaetigt })" focus-class-content>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Name der Schule" :model-value="data.aufnehmdendSchulnummer" @change="aufnehmdendSchulnummer=>patch({ aufnehmdendSchulnummer })" type="text" span="full" />
				<svws-ui-text-input placeholder="Wechseldatum" :model-value="data.aufnehmdendWechseldatum" @change="aufnehmdendWechseldatum=>patch({ aufnehmdendWechseldatum })" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number class="contentFocusField" placeholder="Einschulung" :model-value="data.grundschuleEinschulungsjahr" @change="grundschuleEinschulungsjahr => patch({ grundschuleEinschulungsjahr })" :min="1900" :max="2050" />
				<svws-ui-input-number placeholder="Einschulungsart" :model-value="data.grundschuleEinschulungsartID" @change="grundschuleEinschulungsartID => patch({ grundschuleEinschulungsartID })" />
				<svws-ui-input-number placeholder="EP-Jahre" :model-value="data.grundschuleJahreEingangsphase" @change="grundschuleJahreEingangsphase => patch({ grundschuleJahreEingangsphase })" />
				<svws-ui-input-number placeholder="Übergangsempfehlung Jg. 5" :model-value="data.grundschuleUebergangsempfehlungID" @change="grundschuleUebergangsempfehlungID => patch({ grundschuleUebergangsempfehlungID })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number class="contentFocusField" placeholder="Jahr Wechsel Sek I" :model-value="data.sekIWechsel" @change="sekIWechsel => patch({ sekIWechsel })" :min="1900" :max="2050" />
				<svws-ui-text-input placeholder="Erste Schulform Sek I" :model-value="data.sekIErsteSchulform" @change="sekIErsteSchulform=>patch({ sekIErsteSchulform })" type="text" />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II" :model-value="data.sekIIWechsel" @change="sekIIWechsel => patch({ sekIIWechsel })" :min="1900" :max="2050" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Besondere Merkmale für die Statistik">
			<div>
				<svws-ui-todo />
				<div v-for="merkmal in data.merkmale" :key="merkmal.id">
					<!-- TODO <svws-zu-table> für die Merkmale -->
					<p> {{ merkmal.id + " (" + merkmal.datumVon + "-" + merkmal.datumBis + ")" }} </p>
				</div>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Alle bisher besuchten Schulen">
			<div>
				<svws-ui-todo />
				<div v-for="schule in data.alleSchulen" :key="schule.schulnummer" class="opacity-50">
					<!-- TODO <svws-zu-table> für die besuchten Schulen -->
					<p> {{ `${schule.schulnummer} ... (${schule.datumVon}-${schule.datumBis})` }} </p>
				</div>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { BenutzerKompetenz, Herkunftsarten, Schulform, Schulgliederung } from "@core";
	import type { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';

	const props = defineProps<SchuelerSchulbesuchProps>();

	const schuljahr = computed<number>(() => props.schuelerListeManager().schuelerGetSchuljahrOrException());

	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_ANSEHEN));
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const vorigeArtLetzteVersetzung = computed<Herkunftsarten | undefined>({
		get: () => {
			if (props.data.vorigeArtLetzteVersetzung === null)
				return undefined;
			const artID = +props.data.vorigeArtLetzteVersetzung;
			return Herkunftsarten.getByID(artID) || undefined;
		},
		set: (value) => void props.patch({ vorigeArtLetzteVersetzung:  (value === undefined) ? null : "" + value.daten.id })
	});

	const herkunftsarten = computed<Herkunftsarten[]>(() => {
		return Herkunftsarten.values().filter(h => getBezeichnung(h) !== null);
	});

	const vorigeSchulform = computed<Schulform | undefined>(() => {
		const vorigeAllgHerkunft = props.data.vorigeAllgHerkunft;
		if (vorigeAllgHerkunft === null)
			return undefined;
		const sgl = Schulgliederung.data().getWertByKuerzel(vorigeAllgHerkunft);
		if (sgl !== null)
			return Schulform.BK;
		return Schulform.data().getWertByKuerzel(vorigeAllgHerkunft) || undefined;
	});

	function getBezeichnung(h: Herkunftsarten) {
		return h.getBezeichnung(schuljahr.value, vorigeSchulform.value || Schulform.G);
	}

	const showstatistic = computed(() => true);

</script>
