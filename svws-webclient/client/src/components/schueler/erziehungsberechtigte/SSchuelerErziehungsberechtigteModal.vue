<template>
	<!-- Modal zum Hinzufügen des ersten Erziehungsberechtigten (Position 1) -->
	<svws-ui-modal :show="showModal" @update:show="emit('close-modal')">
		<template #modalTitle>Erziehungsberechtigten hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="text-left">
				<ui-select label="Erzieherart" v-model="selectedErzieherart" :manager="erzieherartenManager" :removable="false" class="col-span-full"
					:readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" searchable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Anrede" v-model="ersterErz.anrede" type="text" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" />
				<svws-ui-text-input placeholder="Titel" v-model="ersterErz.titel" type="text" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" />
				<svws-ui-text-input placeholder="Vorname" v-model="ersterErz.vorname" type="text" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" required />
				<svws-ui-text-input placeholder="Nachname" v-model="ersterErz.nachname" type="text" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" required />
				<svws-ui-text-input placeholder="E-Mail Adresse" v-model="ersterErz.eMail" type="email" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" verify-email />
				<ui-select label="Staatsangehörigkeit" v-model="ersteErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitManager" searchable />

				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße und Hausnummer" v-model="adresse" type="text" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" />
				<ui-select label="Wohnort" v-model="wohnort" :manager="wohnortManager" :disabled="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" searchable />
				<ui-select label="Ortsteil" v-model="ortsteil" :manager="ortsteilManager" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert) || (!ersterErz.wohnortID)" searchable />
				<svws-ui-spacing />
				<svws-ui-tooltip class="col-span-full">
					<svws-ui-text-input v-model="ersterErz.bemerkungen" type="text" placeholder="Bemerkungen" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)" />
					<template #content>
						{{ ersterErz.bemerkungen ?? 'Bemerkungen' }}
					</template>
				</svws-ui-tooltip>
				<svws-ui-checkbox :model-value="ersterErz.erhaeltAnschreiben ?? false" @update:model-value="value => ersterErz.erhaeltAnschreiben = value"
					type="checkbox" title="Erhält Anschreiben" class="col-span-full" :readonly="(!hatKompetenzUpdate) || (istErsterErzGespeichert)">
					Erhält Anschreiben
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<!-- Modal zum Hinzufügen des ersten Erziehungsberechtigten (Position 2) -->
			<div v-if="istErsterErzGespeichert" class="mt-10 border-t pt-6">
				<h4 class="text-xl mb-3">Zweiter Erziehungsberechtigter</h4>
				<svws-ui-input-wrapper :grid="2" class="text-left">
					<svws-ui-text-input placeholder="Anrede" v-model="zweiterErz.anrede" type="text" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Titel" v-model="zweiterErz.titel" type="text" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Vorname" v-model="zweiterErz.vorname" type="text" required :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Nachname" v-model="zweiterErz.nachname" type="text" required :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="E-Mail Adresse" v-model="zweiterErz.eMail" type="email" verify-email :readonly="!hatKompetenzUpdate" />
					<ui-select label="Staatsangehörigkeit" v-model="zweiteErzStaatsangehoerigkeit" :manager="staatsangehoerigkeitManager" searchable />
				</svws-ui-input-wrapper>
			</div>
			<svws-ui-notification type="warning" v-if="mapErzieherarten.size === 0">
				Die Liste der Erzieherarten ist leer, es sollte mindestens eine Erzieherart unter Schule/Kataloge angelegt werden, damit zusätzliche Erzieher eine gültige Zuordnung haben.
			</svws-ui-notification>
			<div class="mt-7 flex flex-row gap-4 justify-between">
				<!-- Erstellt den ersten Erziehungsberechtigten und erweitert das Modal, um einen zweiten Erzieher hinzuzufügen -->
				<svws-ui-tooltip class="col-span-full" v-if="!istErsterErzGespeichert">
					<svws-ui-button
						@click="emit('save-and-show-second')" :disabled="(selectedErzieherart === null) || (mapErzieherarten.size === 0) || (!formIsValid)">
						+ 2. Person
					</svws-ui-button>
					<template #content>
						Einen zweiten Erziehungsberechtigten hinzufügen
					</template>
				</svws-ui-tooltip>

				<div class="flex flex-row gap-4 ml-auto">
					<svws-ui-button type="secondary" @click="emit('close-modal')">Abbrechen</svws-ui-button>
					<!-- Erstellt den ersten Erziehungsberechtigten -->
					<svws-ui-button v-if="!istErsterErzGespeichert" @click="emit('send-request')" :disabled="(selectedErzieherart === null)
						|| (mapErzieherarten.size === 0) || (!formIsValid)">
						Speichern
					</svws-ui-button>
					<!-- Erstellt den zweiten Erzieher -->
					<svws-ui-button v-if="istErsterErzGespeichert" @click="emit('save-second-erzieher')" :disabled="(!stringIsValid(zweiterErz.vorname, true, 120))
						|| (!stringIsValid(zweiterErz.nachname, true, 120)) || (!hatKompetenzUpdate)">
						2. Person speichern
					</svws-ui-button>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { erzieherArtSort, orte_sort, ortsteilSort } from "~/utils/helfer";
	import { AdressenUtils, JavaString, Nationalitaeten } from "@core";
	import type { OrtKatalogEintrag, OrtsteilKatalogEintrag , ErzieherStammdaten, Erzieherart , NationalitaetenKatalogEintrag } from "@core";
	import { computed } from "vue";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<{
		ersterErz: ErzieherStammdaten;
		zweiterErz: ErzieherStammdaten;
		showModal: boolean;
		mapErzieherarten: Map<number, Erzieherart>;
		hatKompetenzUpdate: boolean;
		istErsterErzGespeichert: boolean;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
		schuljahr: number;
	}>();

	const emit = defineEmits<{
		(e: 'erster-erz' | 'zweiter-erz', v: ErzieherStammdaten): void;
		(e: 'send-request' | 'save-and-show-second' | 'save-second-erzieher' | 'close-modal'): void;
	}>();

	const erzieherarten = computed(() => props.mapErzieherarten.values());

	const erzieherartenManager = new SelectManager({ options: erzieherarten.value, sort: erzieherArtSort, optionDisplayText: i => i.bezeichnung, selectionDisplayText: i => i.bezeichnung });

	const selectedErzieherart = computed<Erzieherart | null>({
		get: () => props.mapErzieherarten.get(props.ersterErz.idErzieherArt ?? -1) ?? null,
		set: (erzieherart) => props.ersterErz.idErzieherArt = (erzieherart !== null) ? erzieherart.id : 0,
	})

	const staatsangehoerigkeitManager = new CoreTypeSelectManager({ clazz: Nationalitaeten.class, schuljahr: props.schuljahr, optionDisplayText: "text", selectionDisplayText: "text" });

	const ersteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = props.ersterErz.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(props.schuljahr) ?? null;
		},
		set: (value) => {
			props.ersterErz.staatsangehoerigkeitID = value?.iso3 ?? null
		},
	});

	const zweiteErzStaatsangehoerigkeit = computed<NationalitaetenKatalogEintrag | null>({
		get: (): NationalitaetenKatalogEintrag | null => {
			const iso3 = props.zweiterErz.staatsangehoerigkeitID ?? null;
			return Nationalitaeten.getByISO3(iso3)?.daten(props.schuljahr) ?? null;
		},
		set: (value) => {
			props.zweiterErz.staatsangehoerigkeitID = value?.iso3 ?? null
		},
	});

	const adresse = computed({
		get: () => AdressenUtils.combineStrasse(props.ersterErz.strassenname, props.ersterErz.hausnummer, props.ersterErz.hausnummerZusatz),
		set: (adresse : string) => {
			const vals = AdressenUtils.splitStrasse(adresse);
			props.ersterErz.strassenname = vals[0];
			props.ersterErz.hausnummer = vals[1];
			props.ersterErz.hausnummerZusatz = vals[2];
		},
	})

	const orte = computed(() => props.mapOrte.values());

	const wohnortManager = new SelectManager({ options: orte.value, sort: orte_sort, optionDisplayText: i => `${i.plz} ${i.ortsname}`, selectionDisplayText: i => `${i.plz} ${i.ortsname}` });

	const wohnort = computed<OrtKatalogEintrag | undefined>({
		get: () => ((props.ersterErz.wohnortID === null)) ? undefined : props.mapOrte.get(props.ersterErz.wohnortID),
		set: (value) => props.ersterErz.wohnortID = (value === undefined) ? null : value.id,
	});

	const ortsteile = computed(() => Array.from(props.mapOrtsteile.values()));

	const erzOrtsteileFiltered = computed(() => {
		const wohnortID = props.ersterErz.wohnortID;
		if (wohnortID === null)
			return ortsteile.value;
		return ortsteile.value.filter(o => o.ort_id === wohnortID);
	});

	const erzItems = computed(() => erzOrtsteileFiltered.value);

	const ortsteilManager = new SelectManager({ options: erzItems, sort: ortsteilSort, optionDisplayText: i => i.ortsteil ?? '', selectionDisplayText: i => i.ortsteil ?? '' });

	const ortsteil = computed<OrtsteilKatalogEintrag | null>({
		get: () => props.mapOrtsteile.get(props.ersterErz.ortsteilID ?? -1) ?? null,
		set: (value: OrtsteilKatalogEintrag | null) => props.ersterErz.ortsteilID = value ? value.id : null,
	});

	const formIsValid = computed(() => {
		return Object.keys(props.ersterErz).every((field) => {
			const validateField = fieldIsValid(field as keyof ErzieherStammdaten);
			const fieldValue = props.ersterErz[field as keyof ErzieherStammdaten] as string | null;
			return validateField(fieldValue);
		})
	})

	function fieldIsValid(field: keyof ErzieherStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return stringIsValid(props.ersterErz.nachname, true, 120);
				case 'vorname':
					return stringIsValid(props.ersterErz.vorname, true, 120);
				case 'strassenname':
					return adresseIsValid();
				case 'eMail':
					return stringIsValid(props.ersterErz.eMail, false, 20);
				default:
					return true;
			}
		}
	}

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	function adresseIsValid() {
		return stringIsValid(props.ersterErz.strassenname, false, 55) &&
			stringIsValid(props.ersterErz.hausnummer, false, 10) &&
			stringIsValid(props.ersterErz.hausnummerZusatz, false, 30);
	}

</script>
