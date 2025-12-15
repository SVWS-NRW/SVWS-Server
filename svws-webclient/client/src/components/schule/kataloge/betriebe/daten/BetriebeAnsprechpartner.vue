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
						v-model="data.anrede"
						:valid="() => fieldIsValid('anrede')" :max-len="10" :readonly="!hatKompetenzUpdate" />
					<svws-ui-spacing />
					<svws-ui-text-input placeholder="Rufname"
						v-model="data.rufname"
						:valid="() => fieldIsValid('rufname')" :max-len="80" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Name"
						v-model="data.name"
						:valid="() => fieldIsValid('name')" :max-len="120" :readonly="!hatKompetenzUpdate" required />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="data.telefon"
						:valid="() => fieldIsValid('telefon')" :max-len="20" :readonly="!hatKompetenzUpdate" />
					<svws-ui-text-input placeholder="Email" type="email"
						v-model="data.eMail"
						:valid="() => fieldIsValid('eMail')" :max-len="100" :readonly="!hatKompetenzUpdate" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex gap-4 justify-end">
					<svws-ui-button type="secondary" @click="closeEditingModal">
						Abbrechen
					</svws-ui-button>
					<svws-ui-button @click="sendRequest(currentMode)" :disabled="!formIsValid">
						Speichern
					</svws-ui-button>
				</div>
			</template>
		</svws-ui-modal>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { ArrayList, BetriebeAnsprechpartner, type List } from "@core";
	import type { DataTableColumn, BetriebeListeManager } from "@ui";
	import { phoneNumberIsValid, optionalInputIsValid, emailIsValid, mandatoryInputIsValid } from "~/util/validation/Validation";

	const props = defineProps<{
		manager: () => BetriebeListeManager,
		hatKompetenzUpdate: boolean,
		addAnsprechpartner: (ansprechpartner: Partial<BetriebeAnsprechpartner>) => Promise<void>;
		deleteAnsprechpartner: (ids: List<number>) => Promise<void>;
		patchAnsprechpartner: (id: number, data: Partial<BetriebeAnsprechpartner>) => Promise<void>;
	}>();
	const data = ref<BetriebeAnsprechpartner>(new BetriebeAnsprechpartner());

	function resetData(): void {
		data.value = Object.assign(new BetriebeAnsprechpartner(), { idBetrieb: props.manager().auswahlID() });
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

	const editingModalIsOpen = ref<boolean>(false);

	function openEditingModal() {
		editingModalIsOpen.value = true;
	}

	function closeEditingModal() {
		editingModalIsOpen.value = false;
		setMode(Mode.DEFAULT);
		resetData();
	}

	function addNewEntry() {
		setMode(Mode.ADD);
		resetData();
		openEditingModal();
	}

	function patchEntry(ansprechpartner: BetriebeAnsprechpartner) {
		setMode(Mode.PATCH);
		resetData();
		data.value = Object.assign(data.value, ansprechpartner);
		openEditingModal();
	}

	// --- api ---

	async function sendRequest(type: Mode) {
		const { id, referenziertInAnderenTabellen, ...partial } = data.value;
		if (type === Mode.ADD) {
			await props.addAnsprechpartner(partial);
		}
		if (type === Mode.PATCH) {
			await props.patchAnsprechpartner(data.value.id, partial);
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

	// --- validation ---

	const formIsValid = computed(() => {
		return Object.keys(data.value)
			.every((field: string) => fieldIsValid(field as keyof BetriebeAnsprechpartner));
	});

	const fieldIsValid = (field: keyof BetriebeAnsprechpartner): boolean => {
		switch (field) {
			case 'anrede':
				return optionalInputIsValid(data.value.anrede, 10);
			case 'rufname':
				return optionalInputIsValid(data.value.rufname, 80);
			case 'name':
				return mandatoryInputIsValid(data.value.name, 120);
			case 'telefon':
				return phoneNumberIsValid(data.value.telefon, 20);
			case 'eMail':
				return emailIsValid(data.value.eMail, 100);
			default:
				return true;
		}
	};

</script>
