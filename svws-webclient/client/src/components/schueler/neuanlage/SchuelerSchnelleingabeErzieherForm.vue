<template>
	<svws-ui-content-card class="col-span-full mt-16 lg:mt-20"
		:title="getTitle()">
		<template #actions>
			<svws-ui-checkbox class="mr-2"
				:model-value="data.erhaeltAnschreiben === true"
				@update:model-value="erhaeltAnschreiben => (data !== undefined) && patchErzieher({ erhaeltAnschreiben }, data.id)">
				Erhält Anschreiben
			</svws-ui-checkbox>
		</template>
		<svws-ui-input-wrapper :grid="4">
			<ui-select label="Erzieherart"
				v-model="erzieherart"
				:manager="erzieherartenManager"
				:removable="false" />
			<svws-ui-text-input placeholder="Anrede"
				:model-value="data.anrede"
				@change="patchAnrede"
				:max-len="20" />
			<svws-ui-text-input placeholder="Titel"
				:model-value="data.titel"
				@change="patchTitel"
				:max-len="10" />
			<svws-ui-spacing />
			<svws-ui-text-input placeholder="Name"
				:model-value="data.nachname"
				@change="patchNachname"
				:max-len="120" />
			<svws-ui-text-input placeholder="Vorname"
				:model-value="data.vorname"
				@change="patchVorname" />
			<svws-ui-text-input placeholder="E-Mail Adresse" type="email"
				:model-value="data.eMail"
				@change="patchEmail"
				:valid="emailIsValid"
				:max-len="100" verify-email />
			<svws-ui-spacing />
			<ui-select label="Staatsangehörigkeit"
				v-model="ersterErzStaatsangehoerigkeit"
				:manager="staatsangehoerigkeitenManager"
				searchable />
			<svws-ui-text-input placeholder="Straße und Hausnummer"
				:model-value="adresse"
				@change="patchStrasse"
				:valid="adresseIsValid"
				:max-len="50" />
			<ui-select label="Wohnort"
				v-model="wohnort"
				:manager="wohnortManager"
				searchable :readonly />
			<ui-select label="Ortsteil"
				v-model="ortsteil"
				:manager="ortsteilManager"
				:readonly="(!data.wohnortID || readonly)" />
			<svws-ui-spacing />
			<svws-ui-textarea-input placeholder="Bemerkungen" span="full"
				:model-value="data?.bemerkungen" autoresize
				@change="patchBemerkungen"
				:readonly />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { erzieherArtSort, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { optionalInputIsValid } from "~/util/validation/Validation";
	import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
	import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
	import { ErzieherStammdaten } from "@core/core/data/erzieher/ErzieherStammdaten";
	import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
	import { AdressenUtils } from "@core/core/utils/AdressenUtils";
	import { useAbschnittState } from "@ui/states/AbschnittState";
	import { useOrteState } from "@ui/states/kataloge/OrteState";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import type { SchuelerSchnelleingabeManager } from "@ui/ui/manager/schueler/SchuelerSchnelleingabeManager";

	const props = defineProps<{
		manager: () => SchuelerSchnelleingabeManager;
		data: ErzieherStammdaten | undefined;
		patchErzieher: (data: Partial<ErzieherStammdaten>, idEintrag: number) => Promise<void>;
		readonly: boolean;
	}>();

	const abschnittState = useAbschnittState();
	const orteState = useOrteState();

	const manager = () => props.manager();
	const data = computed<ErzieherStammdaten>(() => props.data ?? new ErzieherStammdaten());
	const erzieherarten = computed(() => manager().erzieherartenById.values());

	const erzieherart = computed({
		get: () => manager().erzieherartenById.get(data.value.idErzieherArt ?? -1) ?? null,
		set: (value) => {
			const id = value?.id ?? null;
			data.value.idErzieherArt = id;
			void props.patchErzieher({ idErzieherArt: id }, data.value.id);
		},
	});

	const wohnort = computed({
		get: () => orteState.orte.byId.get(data.value.wohnortID ?? -1) ?? null,
		set: (value) => {
			data.value.wohnortID = value?.id ?? null;
			void props.patchErzieher({ wohnortID: data.value.wohnortID }, data.value.id);
		},
	});

	const adresse = AdressenUtils.combineStrasse(
		data.value.strassenname ?? "",
		data.value.hausnummer ?? "",
		data.value.hausnummerZusatz ?? ""
	);

	const ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => orteState.ortsteile.byId.get(data.value.ortsteilID ?? -1) ?? null,
		set: (value: OrtsteilKatalogEintrag | null) => {
			data.value.ortsteilID = value?.id ?? null;
			void props.patchErzieher({ ortsteilID: data.value.ortsteilID ?? null }, data.value.id);
		},
	});

	const ersterErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: () => Nationalitaeten.getByISO3(data.value.staatsangehoerigkeitID ?? null)?.daten(abschnittState.auswahl.schuljahr) ?? null,
		set: (value: NationalitaetenKatalogEintrag | null) => {
			const iso3 = value?.iso3 ?? null;
			data.value.staatsangehoerigkeitID = iso3;
			void props.patchErzieher({ staatsangehoerigkeitID: iso3 }, data.value.id);
		},
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		optionDisplayText: i => `${i.plz} ${i.ortsname}`,
		sort: orte_sort, selectionDisplayText: i => `${i.plz} ${i.ortsname}`,
	});

	const erzieherartenManager = new SelectManager({
		options: erzieherarten,
		sort: erzieherArtSort,
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(data.value.wohnortID)),
		sort: ortsteilSort, optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '' });

	// --- validate ---

	function anredeIsValid(anrede: string | null) {
		return optionalInputIsValid(anrede, 20);
	}

	function titelIsValid(titel: string | null) {
		return optionalInputIsValid(titel, 10);
	}

	function nachnameIsValid(nachname: string | null) {
		return optionalInputIsValid(nachname, 120);
	}

	function vornameIsValid(vorname: string | null) {
		return optionalInputIsValid(vorname, 80);
	}

	function emailIsValid(vorname: string | null) {
		return optionalInputIsValid(vorname, 100);
	}

	function adresseIsValid(v: string | null) {
		const [strasse, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
		return optionalInputIsValid(strasse, 50)
			&& optionalInputIsValid(hausnummer, 10)
			&& optionalInputIsValid(hausnummerZusatz, 30);
	}


	// --- patch ---

	function patchAnrede(anrede: string | null) {
		if (anredeIsValid(anrede)) {
			void props.patchErzieher({ anrede }, data.value.id);
		}
	}

	function patchTitel(titel: string | null) {
		if (titelIsValid(titel)) {
			void props.patchErzieher({ titel }, data.value.id);
		}
	}

	function patchNachname(nachname: string | null) {
		if (nachnameIsValid(nachname)) {
			void props.patchErzieher({ nachname }, data.value.id);
		}
	}

	function patchVorname(vorname: string | null) {
		if (vornameIsValid(vorname)) {
			void props.patchErzieher({ vorname }, data.value.id);
		}
	}

	function patchEmail(eMail: string | null) {
		if (emailIsValid(eMail)) {
			void props.patchErzieher({ eMail }, data.value.id);
		}
	}

	function patchStrasse(v: string | null) {
		if (adresseIsValid(v)) {
			const [strassenname, hausnummer, hausnummerZusatz] = AdressenUtils.splitStrasse(v);
			void props.patchErzieher({ strassenname, hausnummer, hausnummerZusatz }, data.value.id);
		}
	}

	function patchBemerkungen(bemerkungen: string | null) {
		void props.patchErzieher({ bemerkungen }, data.value.id);
	}

	// --- util ---

	function getTitle() {
		if (data.value.vorname === null && data.value.nachname === null) {
			return 'Daten zur Person';
		}
		return 'Daten zu ' + data.value.vorname + ' ' + data.value.nachname;
	}

</script>

