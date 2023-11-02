<template>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
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
				<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="resetFachwahlen" />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button type="secondary" @click.prevent="download_file" title="Wahlbogen herunterladen"><i-ri-printer-line />PDF herunterladen</svws-ui-button>
			<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :modus="modus"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :set-wahl="setWahl" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-y-16 lg:gap-y-20">
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
				<s-laufbahnplanung-card-beratung v-if="visible" :gost-laufbahn-beratungsdaten="gostLaufbahnBeratungsdaten" :patch-beratungsdaten="patchBeratungsdaten" :map-lehrer="mapLehrer" :id="id" />
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

	async function download_file() {
		const { data, name } = await props.getPdfWahlbogen();
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
		console.log(data, name)
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
