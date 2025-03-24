<template>
	<div class="page page-flex-row max-w-480">
		<Teleport v-if="hatUpdateKompetenz" defer to=".svws-sub-nav-target">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" @click="export_laufbahnplanung"><span class="icon-sm i-ri-upload-2-line" />Exportieren</svws-ui-button>
				<svws-ui-button type="transparent" @click="show = true"><span class="icon-sm i-ri-download-2-line" /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal v-model:show="show" :import-laufbahnplanung />
				<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="modus === 'normal' ? 'transparent' : 'danger'" @click="switchModus" title="Modus wechseln">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht :gost-jahrgangsdaten :reset-fachwahlen />
				<svws-ui-button type="transparent" @click="switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + textFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport defer to=".svws-ui-header--actions">
			<svws-ui-button-select type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <span class="icon i-ri-printer-line" /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="grow overflow-y-auto overflow-x-hidden min-w-fit">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager :modus :gost-jahrgangsdaten :set-wahl :goto-kursblockung :faecher-anzeigen />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-144 overflow-y-auto overflow-x-hidden">
			<div class="flex flex-col gap-y-16 lg:gap-y-20">
				<s-laufbahnplanung-card-beratung v-if="visible && hatUpdateKompetenz" :gost-laufbahn-beratungsdaten :patch-beratungsdaten="doPatchBeratungsdaten" :list-lehrer :id :schueler :updated />
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager :fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art :set-gost-belegpruefungs-art />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";
	import { BenutzerKompetenz } from "../../../../../core/src/core/types/benutzer/BenutzerKompetenz";
	import type { GostLaufbahnplanungBeratungsdaten } from "../../../../../core/src/core/data/gost/GostLaufbahnplanungBeratungsdaten";

	const props = defineProps<SchuelerLaufbahnplanungProps>();

	const hatUpdateKompetenz = computed<boolean>(() => {
		if ((props.benutzerKompetenzen === undefined) || (props.benutzerKompetenzenAbiturjahrgaenge === undefined) || (props.schueler.abiturjahrgang === null))
			return false;
		return props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (props.benutzerKompetenzen.has(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN) && props.benutzerKompetenzenAbiturjahrgaenge.has(props.schueler.abiturjahrgang));
	});

	const visible = computed<boolean>(() => props.schueler.abiturjahrgang !== null);

	const show = ref<boolean>(false);

	const updated = ref<boolean>(false);
	const curId = ref<number | undefined>()

	watch(() => [props.schueler, props.gostBelegpruefungErgebnis()], ([neu, neu2], [alt, alt2]) => {
		if (alt !== neu) {
			updated.value = false;
			curId.value = undefined;
		}
		if ((neu2 !== alt2) && (updated.value === false) && (curId.value === props.schueler.id))
			updated.value = true;
		else
			curId.value = props.schueler.id;
	})

	async function doPatchBeratungsdaten(data: Partial<GostLaufbahnplanungBeratungsdaten>) {
		await props.patchBeratungsdaten(data);
		updated.value = false;
	}

	const hatFaecherNichtWaehlbar = computed<boolean>(() => {
		for (const fach of props.abiturdatenManager().faecher().faecher())
			if (!(fach.istMoeglichEF1 || fach.istMoeglichEF2 || fach.istMoeglichQ11 || fach.istMoeglichQ12 || fach.istMoeglichQ21 || fach.istMoeglichQ22))
				return true;
		return false;
	});

	async function switchFaecherAnzeigen() {
		switch (props.faecherAnzeigen) {
			case 'alle':
				await props.setFaecherAnzeigen(hatFaecherNichtWaehlbar.value ? 'nur_waehlbare' : 'nur_gewaehlt')
				break;
			case 'nur_waehlbare':
				await props.setFaecherAnzeigen('nur_gewaehlt')
				break;
			case 'nur_gewaehlt':
				await props.setFaecherAnzeigen('alle')
				break;
		}
	}

	function textFaecherAnzeigen() {
		switch (props.faecherAnzeigen) {
			case 'alle':
				return "Alle";
			case 'nur_waehlbare':
				return "Nur wählbare"
			case 'nur_gewaehlt':
				return "Nur gewählte"
		}
	}

	async function switchModus() {
		// wenn EF1 und EF2 bereits festgelegt sind, macht der Hochschreibemodus
		// keinen Sinn mehr und wird deaktiviert.
		const festgelegt = props.gostJahrgangsdaten.istBlockungFestgelegt
		if (festgelegt[0] && festgelegt[1]) {
			switch (props.modus) {
				case 'normal':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
				case 'hochschreiben':
					await props.setModus('normal');
					break;
			}
		} else {
			switch (props.modus) {
				case 'normal':
					await props.setModus('hochschreiben');
					break;
				case 'hochschreiben':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
			}
		}
	}

	const dropdownList = [
		{ text: "Laufbahnwahlbogen", action: () => downloadPDF("Laufbahnwahlbogen"), default: true },
		{ text: "Laufbahnwahlbogen (nur Belegung)", action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)") },
	]

	async function downloadPDF(title: string) {
		const { data, name } = await props.getPdfWahlbogen(title);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function export_laufbahnplanung() {
		const { data, name } = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
