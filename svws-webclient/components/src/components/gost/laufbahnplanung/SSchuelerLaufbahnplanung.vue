<template>
	<div class="page--content page--content--full page--content--laufbahnplanung">
		<Teleport to=".svws-sub-nav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" title="Planung exportieren" @click="export_laufbahnplanung"><i-ri-upload-2-line />Exportieren</svws-ui-button>
				<svws-ui-button type="transparent" title="Planung importieren" @click="showModalImport().value = true"><i-ri-download-2-line /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="importLaufbahnplanung" />
				<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" title="Planung merken" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" title="Planung merken" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="modus === 'normal' ? 'transparent' : 'danger'" @click="switchModus" title="Modus wechseln">
					<i-ri-loop-right-line />
					Modus: <span>{{ modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht :gost-jahrgangsdaten="gostJahrgangsdaten" :reset-fachwahlen="resetFachwahlen" />
				<svws-ui-button type="transparent" title="Fächer anzeigen" @click="switchFaecherAnzeigen()">
					{{ "Fächer anzeigen: " + textFaecherAnzeigen() }}
				</svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button-select type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <i-ri-printer-line /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="flex-grow overflow-y-auto overflow-x-hidden min-w-fit">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :modus="modus"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :set-wahl="setWahl" :goto-kursblockung="gotoKursblockung" :faecher-anzeigen="faecherAnzeigen" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem] overflow-y-auto overflow-x-hidden">
			<div class="flex flex-col gap-y-16 lg:gap-y-20">
				<s-laufbahnplanung-card-beratung v-if="visible" :gost-laufbahn-beratungsdaten="gostLaufbahnBeratungsdaten" :patch-beratungsdaten="patchBeratungsdaten" :map-lehrer="mapLehrer" :id="id" :schueler="schueler" />
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";
	import { computed, onMounted, ref } from "vue";

	const props = defineProps<SchuelerLaufbahnplanungProps>();

	const visible = computed<boolean>(() => props.schueler.abiturjahrgang !== undefined);

	const _showModalImport = ref<boolean>(false);
	const showModalImport = () => _showModalImport;

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

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style lang="postcss" scoped>

	.page--content--laufbahnplanung {
		@apply gap-x-8 2xl:gap-x-12 relative overflow-y-hidden h-full;
		@apply px-4 lg:px-6 3xl:px-8 4xl:px-12 pt-8 pb-8;
	}

</style>
