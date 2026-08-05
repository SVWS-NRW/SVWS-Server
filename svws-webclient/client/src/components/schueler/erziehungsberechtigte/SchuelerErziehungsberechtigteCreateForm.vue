<template>
	<svws-ui-modal :show="createModalIsOpen" @update:show="closeModal">
		<template #modalTitle>Erziehungsberechtigten hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2" class="text-left" v-if="!istErsterErzGespeichert">
				<ui-select label="Erzieherart"
					v-model="model.erzieherart.value"
					:manager="erzieherartenManager"
					:removable="false" class="col-span-full" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Anrede" v-model="model.proxy.anrede" type="text" />
				<svws-ui-text-input placeholder="Titel" v-model="model.proxy.titel" type="text" />
				<svws-ui-text-input placeholder="Rufname" v-model="model.proxy.vorname"
					:validation="() => model.getFehler('vorname')" type="text" required />
				<svws-ui-text-input placeholder="Nachname" v-model="model.proxy.nachname"
					:validation="() => model.getFehler('nachname')" type="text" required />
				<svws-ui-text-input placeholder="E-Mail Adresse" v-model="model.proxy.eMail"
					:validation="() => model.getFehler('eMail')" type="email" verify-email />
				<ui-select label="Staatsangehörigkeit"
					v-model="model.staatsangehoerigkeit.value"
					:manager="staatsangehoerigkeitenManager" searchable />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Straße und Hausnummer"
					v-model="model.adresse.value"
					:validation="() => model.getFehler('strassenname')" />
				<ui-select label="Wohnort" v-model="model.wohnort.value" :manager="wohnortManager" searchable />
				<ui-select label="Ortsteil" v-model="model.ortsteil.value" :manager="ortsteilManager"
					:readonly="!model.proxy.wohnortID" searchable />
				<svws-ui-spacing />
				<svws-ui-tooltip class="col-span-full">
					<svws-ui-text-input v-model="model.proxy.bemerkungen" type="text" placeholder="Bemerkungen" />
					<template #content>{{ model.proxy.bemerkungen ?? 'Bemerkungen' }}</template>
				</svws-ui-tooltip>
				<svws-ui-checkbox :model-value="model.proxy.erhaeltAnschreiben ?? false"
					@update:model-value="val => model.proxy.erhaeltAnschreiben = val"
					type="checkbox" title="Erhält Anschreiben" class="col-span-full">
					Erhält Anschreiben
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<div v-if="istErsterErzGespeichert">
				<schueler-erziehungsberechtigte-zweiter-erz-felder :model="zweiterErzModel" :schuljahr :readonly="false" />
			</div>
			<svws-ui-notification type="warning" v-if="erzieherartenById.size === 0">
				Die Liste der Erzieherarten ist leer, es sollte mindestens eine Erzieherart unter Schule/Kataloge angelegt werden, damit zusätzliche Erzieher eine gültige Zuordnung
			</svws-ui-notification>
			<div class="mt-7 flex flex-row gap-4 justify-between">
				<svws-ui-tooltip class="col-span-full" v-if="!istErsterErzGespeichert">
					<svws-ui-button @click="saveAndShowSecondForm" :disabled="!formIsValid">
						+ 2. Person
					</svws-ui-button>
					<template #content>Einen zweiten Erziehungsberechtigten hinzufügen</template>
				</svws-ui-tooltip>
				<div class="flex flex-row gap-4 ml-auto">
					<svws-ui-button type="secondary" @click="closeModal">Abbrechen</svws-ui-button>
					<svws-ui-button v-if="!istErsterErzGespeichert" @click="sendRequest" :disabled="!formIsValid">
						Speichern
					</svws-ui-button>
					<svws-ui-button v-if="istErsterErzGespeichert" @click="saveSecondErzieher"
						:disabled="zweiterErzModel.hatFehler()">
						2. Person speichern
					</svws-ui-button>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>


<script setup lang="ts">
	import { computed, ref, shallowRef } from "vue";
	import type { Erzieherart } from "@core";
	import { ErzieherStammdaten, Nationalitaeten } from "@core";
	import { CoreTypeSelectManager, SelectManager, useOrteState } from "@ui";
	import { ErzieherStammdatenModelProxy } from "~/components/schueler/erziehungsberechtigte/modelproxy/ErzieherStammdatenModelProxy";
	import SchuelerErziehungsberechtigteZweiterErzFelder from "./SchuelerErziehungsberechtigteZweiterErzFelder.vue";
	import { erzieherArtSort, orte_sort, ortsteilSort } from "~/utils/helfer";

	const props = defineProps<{
		addErzieher: (data: Partial<ErzieherStammdaten>, pos: number) => Promise<ErzieherStammdaten>;
		patchErzieherAnPosition: (data: Partial<ErzieherStammdaten>, id: number, pos: number) => Promise<void>;
		erzieherartenById: Map<number, Erzieherart>;
		schuljahr: number;
		createModalIsOpen: boolean;
	}>();

	const emit = defineEmits<{
		'closeModal': [];
	}>();

	const orteState = useOrteState();

	const model = shallowRef(createModel());
	const formIsValid = computed(() => model.value.getAlleFehler().isEmpty());

	const istErsterErzGespeichert = ref(false);

	const erzieherartenManager = new SelectManager({
		options: computed(() => props.erzieherartenById.values()),
		sort: erzieherArtSort,
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		sort: orte_sort,
		optionDisplayText: i => `${i.plz} ${i.ortsname}`,
		selectionDisplayText: i => `${i.plz} ${i.ortsname}`,
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.list),
		sort: ortsteilSort,
		optionDisplayText: i => i.ortsteil ?? '',
		selectionDisplayText: i => i.ortsteil ?? '',
	});

	const staatsangehoerigkeitenManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		schuljahr: computed(() => props.schuljahr),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	});

	function createModel() {
		const defaultErz = new ErzieherStammdaten();
		const ersteErzieherArt = props.erzieherartenById.values().next().value;
		defaultErz.idErzieherArt = ersteErzieherArt?.id ?? null;
		defaultErz.erhaeltAnschreiben = false;
		return new ErzieherStammdatenModelProxy(
			() => defaultErz,
			() => props.erzieherartenById,
			() => props.schuljahr
		);
	}

	const zweiterErzModel = shallowRef(createZweiterModel());

	function createZweiterModel() {
		const defaultZweiterErz = new ErzieherStammdaten();
		return new ErzieherStammdatenModelProxy(
			() => defaultZweiterErz,
			() => props.erzieherartenById,
			() => props.schuljahr
		);
	}


	function resetForm() {
		model.value = createModel();
		zweiterErzModel.value = createZweiterModel();
		istErsterErzGespeichert.value = false;
	}

	function closeModal() {
		resetForm();
		emit('closeModal');
	}

	async function sendRequest() {
		const { id, idSchueler, ...data } = model.value.proxy;
		await props.addErzieher(data, 1);
		closeModal();
	}

	async function saveAndShowSecondForm() {
		const { id, idSchueler, ...data } = model.value.proxy;
		const saved = await props.addErzieher(data, 1);
		zweiterErzModel.value.proxy.idErzieherArt = model.value.proxy.idErzieherArt;
		zweiterErzModel.value.proxy.wohnortID = model.value.proxy.wohnortID;
		zweiterErzModel.value.proxy.ortsteilID = model.value.proxy.ortsteilID;
		zweiterErzModel.value.proxy.bemerkungen = model.value.proxy.bemerkungen;
		zweiterErzModel.value.proxy.strassenname = model.value.proxy.strassenname;
		zweiterErzModel.value.proxy.hausnummer = model.value.proxy.hausnummer;
		zweiterErzModel.value.proxy.hausnummerZusatz = model.value.proxy.hausnummerZusatz;
		model.value.proxy.id = saved.id;
		istErsterErzGespeichert.value = true;
	}

	async function saveSecondErzieher() {
		const { id, idSchueler, erhaeltAnschreiben, ...data } = zweiterErzModel.value.proxy;
		await props.patchErzieherAnPosition(data, model.value.proxy.id, 2);
		closeModal();
	}

</script>
