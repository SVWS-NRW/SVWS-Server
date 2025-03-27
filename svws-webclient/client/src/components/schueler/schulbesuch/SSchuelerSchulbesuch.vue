<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Vor der Aufnahme besucht">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete :item-filter="filterSchulenEintraege"
					:model-value="manager().getVorherigeSchule()" @update:model-value="v => manager().patchSchule(v, 'idVorherigeSchule')" removable />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idVorherigeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>
				<svws-ui-text-input placeholder="allgemeine Herkunft" :model-value="manager().getVorigeAllgHerkunft()" readonly />
				<svws-ui-text-input v-autofocus placeholder="Statistik-Schulnummer" :statistics="true" readonly
					:model-value="manager().getVorherigeSchule()?.schulnummerStatistik ?? ' - '" />
				<svws-ui-text-input placeholder="Entlassen am" type="date" :model-value="manager().daten.vorigeEntlassdatum"
					@change="vorigeEntlassdatum => manager().doPatch({ vorigeEntlassdatum })" />
				<svws-ui-select title="Entlassjahrgang" :items="Jahrgaenge.values()" :model-value="manager().getEntlassjahrgang('vorigeEntlassjahrgang')"
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'vorigeEntlassjahrgang')" :item-text="textJahrgang" removable />
				<svws-ui-text-input placeholder="Bemerkung" span="full" :model-value="manager().daten.vorigeBemerkung" :max-len="255"
					@change="v => { if ((v ?? '').length <= 255) manager().doPatch({ vorigeBemerkung : v }) } " />
				<svws-ui-spacing />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('vorigeEntlassgrundID')"
					@update:model-value="v => manager().patchEntlassgrund(v, 'vorigeEntlassgrundID')" />
				<svws-ui-text-input placeholder="höchster Abschluss, der von der anderen Schule mitgebracht wurde" disabled
					@change="vorigeAbschlussartID => manager().doPatch({ vorigeAbschlussartID })" :model-value="manager().daten.vorigeAbschlussartID" />
				<svws-ui-select class="col-span-full" title="Versetzung" :items="manager().getHerkunftsarten()" :item-text="textHerkunftsarten" removable
					:model-value="manager().getVorigeArtLetzteVersetzung()" @update:model-value="v => manager().patchVorigeArtLetzteVersetzung(v)" :statistics="true" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Entlassung von eigener Schule">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input class="contentFocusField" placeholder="Entlassen am" type="date" :model-value="manager().daten.entlassungDatum"
					@change="entlassungDatum => manager().doPatch({ entlassungDatum })" />
				<svws-ui-select title="Entlassjahrgang" :items="Jahrgaenge.values()" :model-value="manager().getEntlassjahrgang('entlassungJahrgang')"
					@update:model-value="v => manager().patchEntlassjahrgang(v, 'entlassungJahrgang')" :item-text="textJahrgang" removable />
				<svws-ui-select title="Entlassgrund" :items="manager().entlassgruendeById" :item-text="v => v.bezeichnung" removable
					:model-value="manager().getEntlassgrund('entlassungGrundID')"
					@update:model-value="v => manager().patchEntlassgrund(v, 'entlassungGrundID')" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Art des Abschlusses" span="full" :model-value="manager().daten.entlassungAbschlussartID" disabled
					@change="entlassungAbschlussartID => manager().doPatch({ entlassungAbschlussartID })" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wechsel zu aufnehmender Schule">
			<template #actions>
				<svws-ui-checkbox :model-value="manager().daten.aufnehmendBestaetigt === true" :indeterminate="manager().daten.aufnehmendBestaetigt === null"
					@update:model-value="aufnehmendBestaetigt => manager().doPatch({ aufnehmendBestaetigt })" focus-class-content>
					Aufnahme bestätigt
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select title="Schule" :items="manager().schulenById.values()" :item-text="textSchule" autocomplete
					:model-value="manager().schulenById.get(manager().daten.idAufnehmendeSchule ?? -1)" :item-filter="filterSchulenEintraege" removable
					@update:model-value="v => manager().patchSchule(v, 'idAufnehmendeSchule')" />
				<svws-ui-button type="transparent" @click="goToSchule(manager().daten.idAufnehmendeSchule ?? -1)">
					<span class="icon i-ri-link" />Zur Schule
				</svws-ui-button>

				<svws-ui-text-input placeholder="Wechseldatum" :model-value="manager().daten.aufnehmendWechseldatum"
					@change="aufnehmendWechseldatum => manager().doPatch({ aufnehmendWechseldatum })" type="date" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Grundschulbesuch">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-number class="contentFocusField" placeholder="Einschulung" :model-value="manager().daten.grundschuleEinschulungsjahr"
					@change="grundschuleEinschulungsjahr => manager().doPatch({ grundschuleEinschulungsjahr })" :min="1900" :max="2100" />
				<svws-ui-select disabled title="Einschulungsart" :items="Einschulungsart.values()" :model-value="manager().getEinschulungsart()"
					@update:model-value="v => manager().doPatch({ grundschuleEinschulungsartID : v?.daten.id ?? null })" :item-text="textEinschulungsart" />
				<svws-ui-select title="EP-Jahre" :items="PrimarstufeSchuleingangsphaseBesuchsjahre.values()" removable :item-text="textEPJahre" :model-value="manager().getEPJahre()"
					@update:model-value="v => manager().doPatch({grundschuleJahreEingangsphase : Number(v?.daten(manager().schuljahr)?.schluessel ?? null)})" />
				<svws-ui-select title="Übergangsempfehlung Jg. 5" :items="Uebergangsempfehlung.values()" :item-text="textUebergangsempfehlung" removable
					@update:model-value="v => manager().doPatch({kuerzelGrundschuleUebergangsempfehlung : v?.daten(manager().schuljahr)?.kuerzel ?? null})"
					:model-value="manager().getUebergangsempfehlung()" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sekundarstufe I">
			<svws-ui-input-wrapper>
				<svws-ui-input-number class="contentFocusField" placeholder="Jahr Wechsel Sek I" :model-value="manager().daten.sekIWechsel"
					@change="sekIWechsel => manager().doPatch({ sekIWechsel })" :min="1900" :max="2100" />
				<svws-ui-select title="Erste Schulform Sek I" :items="Schulform.values()" :item-text="textSchulformSek1" :model-value="manager().getSchulformSek1()"
					@update:model-value="v => manager().doPatch({ sekIErsteSchulform : v?.daten(manager().schuljahr)?.kuerzel ?? null })" />
				<svws-ui-input-number placeholder="Jahr Wechsel Sek II" :model-value="manager().daten.sekIIWechsel"
					@change="sekIIWechsel => manager().doPatch({ sekIIWechsel })" :min="1900" :max="2100" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Besondere Merkmale für die Statistik">
			<svws-ui-table :columns="colsMerkmale" :items="manager().daten.merkmale">
				<template #cell(merkmal)="{ rowData: s }">
					<span>{{ manager().merkmaleById.get(s.idMerkmal ?? -1)?.bezeichnung ?? " - " }}</span>
				</template>
				<template #cell(datumVon)="{ rowData: s }">
					<span>{{ formatDate(s.datumVon) }}</span>
				</template>
				<template #cell(datumBis)="{ rowData: s }">
					<span>{{ formatDate(s.datumBis) }}</span>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Alle bisher besuchten Schulen" class="col-span-full">
			<svws-ui-table :columns="colsSchulen" :items="manager().daten.alleSchulen">
				<template #cell(schulform)="{ rowData: s}">
					<span>{{ Schulform.data().getEintragByID(manager().schulenById.get(s.id)?.idSchulform ?? -1)?.kuerzel ?? " - " }}</span>
				</template>
				<template #cell(schulname)="{ rowData: s }">
					<span>{{ manager().schulenById.get(s.id)?.name ?? " - " }}</span>
				</template>
				<template #cell(datumVon)="{ rowData: s }">
					<span>{{ formatDate(s.datumVon) }}</span>
				</template>
				<template #cell(datumBis)="{ rowData: s }">
					<span>{{ formatDate(s.datumBis) }}</span>
				</template>
				<template #cell(entlassart)="{ rowData: s }">
					<span>{{ manager().entlassgruendeById.get(s.entlassgrundID ?? -1)?.bezeichnung ?? " - " }}</span>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { Einschulungsart, Jahrgaenge, PrimarstufeSchuleingangsphaseBesuchsjahre, Schulform, Uebergangsempfehlung } from "@core";
	import type { Herkunftsarten, SchulEintrag } from "@core";
	import type { SchuelerSchulbesuchProps } from './SSchuelerSchulbesuchProps';
	import type { DataTableColumn } from "@ui";
	import { filterSchulenEintraege } from "~/utils/helfer";

	const props = defineProps<SchuelerSchulbesuchProps>();

	function textHerkunftsarten(h: Herkunftsarten) {
		return h.getBezeichnung(props.manager().schuljahr, props.manager().getVorigeSchulform() || Schulform.G) + ' (' + h.daten.kuerzel + ')';
	}

	function textSchule(s: SchulEintrag) {
		return s.name;
	}

	function textJahrgang(j : Jahrgaenge) {
		return j.daten(props.manager().schuljahr)?.kuerzel ?? '-';
	}

	function textEinschulungsart(e : Einschulungsart) {
		return e.daten.kuerzel + ' - '+ e.daten.beschreibung;
	}

	function textEPJahre(p : PrimarstufeSchuleingangsphaseBesuchsjahre) {
		return p.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function textUebergangsempfehlung(u : Uebergangsempfehlung) {
		return u.daten(props.manager().schuljahr)?.text ?? '-';
	}

	function textSchulformSek1(s : Schulform) {
		return s.daten(props.manager().schuljahr)?.text ?? '-';
	}

	const colsSchulen: DataTableColumn[] = [
		{ key: "schulform", label: "Schulform", span: 0.2, align: "center" },
		{ key: "schulname", label: "Schulname" },
		{ key: "datumVon", label: "Aufnahme-Datum", span: 0.25, align: "center" },
		{ key: "jahrgangVon", label: "Jahrgang", span: 0.15, align: "center" },
		{ key: "datumBis", label: "Entlass-Datum", span: 0.25, align: "center" },
		{ key: "jahrgangBis", label: "Jahrgang", span: 0.15, align: "center" },
		{ key: "entlassart", label: "Entlassart", align: "center" },
	];

	const colsMerkmale: DataTableColumn[] = [
		{ key: "merkmal", label: "Merkmal"},
		{ key: "datumVon", label: "Von"},
		{ key: "datumBis", label: "Bis"},
	]

	function formatDate(dateString: string | null): string {
		if (dateString === null)
			return "";
		return new Date(dateString).toLocaleDateString("de-DE");
	}

</script>
