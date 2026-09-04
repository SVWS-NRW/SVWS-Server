<template>
	<svws-ui-content-card title="Ansprechpartner">
		<!-- Table -->
		<svws-ui-table :columns
			:items="ansprechpartner"
			v-model="entriesToBeDeleted"
			clickable @update:clicked="patchEntry"
			:selectable="hatKompetenzUpdate" count>
			<template #cell(referenziertInAnderenTabellen)="{ value, rowData }">
				<div v-if="value && idsSelectedEntries.has(rowData.id)">
					<svws-ui-tooltip>
						<span class="icon icon-ui-danger i-ri-alert-line" />
						<template #content>
							Dieser Ansprechpartner ist an anderer Stelle referenziert und kann daher nicht gelöscht werden.
						</template>
					</svws-ui-tooltip>
				</div>
			</template>
			<template #actions v-if="hatKompetenzUpdate">
				<div class="inline-flex gap-4">
					<svws-ui-button @click="addNewEntry" type="icon">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
					<svws-ui-button type="trash"
						@click="deleteSelected"
						:disabled="nothingSelected || !allEntriesDeletable" />
				</div>
			</template>
		</svws-ui-table>
		<!-- Editing Modal -->
		<svws-ui-modal :show="editingModalIsOpen" size="medium"
			:auto-close="false" :close-in-title="false">
			<template #modalTitle>Ansprechpartner</template>
			<template #modalContent>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Anrede"
						v-model="data.proxy.anrede"
						:validation="() => data.getFehler('anrede')"
						@change="data.patch"
						skip-default-validation
						:max-len="10" :readonly />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Rufname"
						v-model="data.proxy.rufname"
						:validation="() => data.getFehler('rufname')"
						@change="data.patch"
						skip-default-validation
						:max-len="80" :readonly />
					<svws-ui-text-input placeholder="Name"
						v-model="data.proxy.name"
						:validation="() => data.getFehler('name')"
						@change="data.patch"
						skip-default-validation
						:max-len="120" :readonly required />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="data.proxy.telefon"
						:validation="() => data.getFehler('telefon')"
						@change="data.patch"
						skip-default-validation
						:max-len="20" :readonly />
					<svws-ui-text-input placeholder="Email" type="email"
						v-model="data.proxy.eMail"
						:validation="() => data.getFehler('eMail')"
						@change="data.patch"
						skip-default-validation
						:max-len="100" :readonly />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex gap-4 justify-end">
					<svws-ui-button type="secondary" @click="closeEditingModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest(currentMode)" :disabled="!isValid">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { BetriebeAnsprechpartner } from "@core/core/data/schule/BetriebeAnsprechpartner";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import type { DataTableColumn } from "@ui/types";
	import type { BetriebeListeManager } from "@ui/ui/manager/kataloge/BetriebeListeManager";
	import { computed, ref } from "vue";
	import { BetriebeAnsprechpartnerModelProxy } from "~/components/schule/kataloge/betriebe/modelproxy/BetriebeAnsprechpartnerModelProxy";

	const props = defineProps<{
		manager: () => BetriebeListeManager,
		hatKompetenzUpdate: boolean,
		addAnsprechpartner: (ansprechpartner: Partial<BetriebeAnsprechpartner>) => Promise<void>;
		deleteAnsprechpartner: (ids: List<number>) => Promise<void>;
		patchAnsprechpartner: (data: Partial<BetriebeAnsprechpartner>) => Promise<boolean>;
	}>();

	let data = new BetriebeAnsprechpartnerModelProxy(() => new BetriebeAnsprechpartner(), props.patchAnsprechpartner);
	const isValid = computed<boolean>(() => data.getAlleFehler().isEmpty());
	const readonly = !props.hatKompetenzUpdate;

	function resetData(): void {
		data = new BetriebeAnsprechpartnerModelProxy(() => new BetriebeAnsprechpartner(), props.patchAnsprechpartner);
		const idBetrieb = props.manager().auswahlID();
		if (idBetrieb !== null) {
			data.proxy.idBetrieb = props.manager().auswahlID() ?? -1;
		}
	}

	// --- table ---

	const ansprechpartner = computed(() => [...props.manager().daten().ansprechpartner]);
	const entriesToBeDeleted = ref<BetriebeAnsprechpartner[]>([]);
	const idsSelectedEntries = computed<Set<number>>(() => new Set<number>(entriesToBeDeleted.value.map(e => e.id)));
	const nothingSelected = computed<boolean>(() => entriesToBeDeleted.value.length === 0);
	const allEntriesDeletable = computed<boolean>(() => {
		for (const ap of entriesToBeDeleted.value) {
			if (ap.referenziertInAnderenTabellen) {
				return false;
			}
		}
		return true;
	});

	const columns: DataTableColumn[] = [
		{ key: "referenziertInAnderenTabellen", label: "", fixedWidth: 2 },
		{ key: "anrede", label: "Anrede", fixedWidth: 4 },
		{ key: "rufname", label: "Rufname", span: 3 },
		{ key: "name", label: "Name", span: 4 },
		{ key: "eMail", label: "Email", span: 4 },
		{ key: "telefon", label: "Telefon", span: 3 },
	];

	// --- add | patch ---

	function addNewEntry() {
		setMode(Mode.ADD);
		resetData();
		openEditingModal();
	}

	function patchEntry(ansprechpartner: BetriebeAnsprechpartner) {
		setMode(Mode.PATCH);
		resetData();
		data = new BetriebeAnsprechpartnerModelProxy(() => ansprechpartner);
		openEditingModal();
	}

	// --- api ---

	async function sendRequest(type: Mode) {
		if (type === Mode.ADD) {
			const { id, referenziertInAnderenTabellen, ...partial } = data.proxy;
			await props.addAnsprechpartner(partial);
		}
		if (type === Mode.PATCH) {
			const { referenziertInAnderenTabellen, ...partial } = data.proxy;
			await props.patchAnsprechpartner(partial);
		}
		setMode(Mode.DEFAULT);
		closeEditingModal();
	}

	async function deleteSelected() {
		if (entriesToBeDeleted.value.length === 0) {
			return;
		}
		const ids = new ArrayList<number>();
		for (const s of entriesToBeDeleted.value) {
			ids.add(s.id);
		}
		await props.deleteAnsprechpartner(ids);
		entriesToBeDeleted.value = [];
	}

	// --- mode ---
	enum Mode { ADD, PATCH, DEFAULT }
	const currentMode = ref<Mode>(Mode.DEFAULT);

	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	const editingModalIsOpen = ref<boolean>(false);

	function openEditingModal() {
		editingModalIsOpen.value = true;
	}

	function closeEditingModal() {
		editingModalIsOpen.value = false;
		setMode(Mode.DEFAULT);
		resetData();
	}

</script>
