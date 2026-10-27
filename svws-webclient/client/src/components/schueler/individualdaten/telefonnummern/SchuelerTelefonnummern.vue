<template>
	<svws-ui-content-card title="Weitere Telefonnummern">
		<schueler-telefonnummern-table @delete-telefonnummern="deleteTelefonnummern"
			@open-patch-modal="editTelefonnummer"
			@add-modal="addTelefonnummer()"
			:readonly />
		<schueler-telefonnummern-modal v-if="model !== null" :show="showModalTelefonnummer"
			:data="model" @save="saveTelefonnummer()"
			@close-modal="closeModalTelefonnummer"
			:is-patch="currentTelefonnummernMode === Mode.PATCH"
			:readonly />
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { ref, shallowRef } from "vue";

	import { SchuelerTelefon } from "@core/core/data/schueler/SchuelerTelefon";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useTelefonartenState } from "@ui/states/kataloge/TelefonartenState";

	import { SchuelerTelefonnummerModelProxy } from "../modelproxy/SchuelerTelefonnummerModelProxy";
	import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";

	import type { SchuelerTelefonnummernProps } from "./SchuelerTelefonnummernProps";

	const props = defineProps<SchuelerTelefonnummernProps>();
	const schuelerAuswahlState = useSchuelerAuswahlState();
	const telefonartenState = useTelefonartenState();

	enum Mode { ADD, PATCH, DEFAULT }

	const currentTelefonnummernMode = ref<Mode>(Mode.DEFAULT);
	const showModalTelefonnummer = ref<boolean>(false);

	const model = shallowRef<SchuelerTelefonnummerModelProxy | null>(null);

	function setDefaultCreateModel(): void {
		const defaultTelefon = new SchuelerTelefon();
		defaultTelefon.telefonnummer = '+49';
		const ersteTelefonArt = telefonartenState.telefonarten.byId.values().next().value;
		defaultTelefon.idTelefonArt = ersteTelefonArt?.id ?? -1;
		model.value = new SchuelerTelefonnummerModelProxy(
			() => defaultTelefon,
			() => telefonartenState.telefonarten
		);
	}

	function setTelefonnummernMode(newMode: Mode): void {
		currentTelefonnummernMode.value = newMode;
	}

	function openModalTelefonnummer(): void {
		showModalTelefonnummer.value = true;
	}

	function closeModalTelefonnummer(): void {
		model.value = null;
		setTelefonnummernMode(Mode.DEFAULT);
		showModalTelefonnummer.value = false;
	}

	function enterDefaultMode(): void {
		setTelefonnummernMode(Mode.DEFAULT);
		closeModalTelefonnummer();
	}

	function addTelefonnummer(): void {
		setDefaultCreateModel();
		setTelefonnummernMode(Mode.ADD);
		openModalTelefonnummer();
	}

	function editTelefonnummer(telefonnummer: SchuelerTelefon): void {
		model.value = new SchuelerTelefonnummerModelProxy(
			() => telefonnummer,
			() => telefonartenState.telefonarten,
			(data) => schuelerAuswahlState.patchTelefoneintrag(data, telefonnummer.id)
		);
		setTelefonnummernMode(Mode.PATCH);
		openModalTelefonnummer();
	}

	async function saveTelefonnummer(): Promise<void> {
		if (model.value === null) {
			return;
		}
		if (currentTelefonnummernMode.value === Mode.ADD) {
			const { id, idSchueler, ...data } = model.value.proxy;
			await schuelerAuswahlState.addTelefoneintrag(data, props.idSchueler);
		}

		if (currentTelefonnummernMode.value === Mode.PATCH) {
			await model.value.patch();
		}
		enterDefaultMode();
	}

	async function deleteTelefonnummern(selectedTelefonnummern: SchuelerTelefon[]): Promise<void> {
		if (selectedTelefonnummern.length === 0) {
			return;
		}

		const ids = new ArrayList<number>();
		for (const telefonnummer of selectedTelefonnummern) {
			ids.add(telefonnummer.id);
		}

		await schuelerAuswahlState.deleteTelefoneintrage(ids);
	}

</script>
