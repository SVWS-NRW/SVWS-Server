<template>
	<!-- optional slot to render the button and provide a method to open the modal -->
	<slot :open-modal />

	<div class="absolute">
		<svws-ui-modal v-model:show="show" size="medium">
			<template #modalTitle>
				<span>
					Eintrag zur Wiedervorlage {{ mode === "create" ? 'anlegen' : 'bearbeiten' }}
				</span>
			</template>

			<template #modalContent>
				<svws-ui-content-card>
					<h3 class="text-left flex items-center">
						<template v-if="type === 'allgemein'">
							<span>Allgemein</span>
						</template>
						<template v-else-if="personName">
							<span :class="`icon ${icon} mr-2`" />
							<span>{{ personName }}</span>
						</template>
					</h3>

					<svws-ui-input-wrapper :grid="2">
						<svws-ui-textarea-input placeholder="Bemerkung"
							v-model="modelProxy.proxy.bemerkung"
							:validation="() => modelProxy.getFehler('bemerkung')" />

						<div class="flex flex-col">
							<svws-ui-text-input type="date"
								placeholder="Wiedervorlage am"
								v-model="modelProxy.proxy.tsWiedervorlage"
								:validation="() => modelProxy.getFehler('tsWiedervorlage')"
								removable />

							<svws-ui-select label="Sichtbar für folgenden Benutzergruppen"
								v-model="modelProxy.proxy.idBenutzergruppe"
								:empty-text="() => 'keine'"
								:disabled="wiedervorlageState.benutzerGruppen.isEmpty()"
								:items="wiedervorlageState.benutzerGruppen"
								:item-text="item => item.bezeichnung"
								removable />
							<svws-ui-checkbox v-model="modelProxy.proxy.automatischErledigt">Eintrag automatisch löschen</svws-ui-checkbox>
						</div>
					</svws-ui-input-wrapper>
				</svws-ui-content-card>
			</template>

			<template #modalActions>
				<svws-ui-button type="secondary" @click="closeModal()">Abbrechen</svws-ui-button>
				<svws-ui-button type="primary" @click="submit()" :disabled="hatFehler">
					{{ mode === "create" ? 'Anlegen' : 'Speichern' }}
				</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">
	import { computed, watch } from 'vue';
	import type { WiedervorlageEintrag } from "@core";
	import { dateTodayPlus, formatDateToDateTime } from "~/utils/date";
	import type { Wiedervorlage } from "~/components/wiedervorlage/Wiedervorlage";
	import { WiedervorlageModelProxy } from "~/components/wiedervorlage/WiedervorlageModelProxy";
	import { useWiedervorlageState } from "@ui";
	const wiedervorlageState = useWiedervorlageState();

	const props = withDefaults(defineProps<{
		personId?: number,
		personName?: string,
		mode?: "create" | "edit",
		type?: "allgemein" | "schueler" | "lehrkraft" | "erzieher",
	}>(), {
		mode: "create",
		type: "allgemein",
		personName: undefined,
		personId: undefined,
	});

	const emit = defineEmits<{
		// event when new entry was created
		created: [val: WiedervorlageEintrag];
		// even when an entry was changed
		updated: [val: WiedervorlageEintrag];
	}>();

	const show = defineModel({ type: Boolean, default: false });

	const defaultValue: Wiedervorlage = {
		idBenutzergruppe: null,
		typPerson: null,
		idPerson: null,
		bemerkung: "",
		tsWiedervorlage: null,
		automatischErledigt: true,
	};

	const modelProxy = new WiedervorlageModelProxy(() => defaultValue);

	const hatFehler = computed(() => {
		return modelProxy.getAlleFehler().size() > 0;
	});

	const icon = computed(() => {
		if (props.type === "schueler" || props.type === "erzieher") {
			return "i-ri-group-line";
		}
		if (props.type === "lehrkraft") {
			return "i-ri-briefcase-line";
		}
		return null;
	});

	function getTypPerson() {
		switch (props.type) {
			case "lehrkraft":
				return 1;
			case "schueler":
				return 2;
			case "erzieher":
				return 3;
			default:
				return null;
		}
	}

	function setInitialData() {
		modelProxy.proxy.idPerson = props.personId ?? null;
		modelProxy.proxy.typPerson = getTypPerson();

		if (props.mode === "create") {
			modelProxy.proxy.tsWiedervorlage = dateTodayPlus({ days: 7 });
		}
	}

	function resetModal() {
		modelProxy.reset();
		setInitialData();
	}

	async function openModal() {
		// open Modal
		show.value = true;
	}

	function closeModal() {
		show.value = false;
	}

	async function submit() {
		const submitData: Partial<WiedervorlageEintrag> = {
			...modelProxy.proxy,
			tsWiedervorlage: modelProxy.proxy.tsWiedervorlage === null ? null : (formatDateToDateTime(modelProxy.proxy.tsWiedervorlage) ?? null),
			idBenutzergruppe: modelProxy.proxy.idBenutzergruppe?.id ?? null,
		};

		if (props.mode === "create") {
			await create(submitData);
		} else {
			await update(submitData);
		}
	}

	async function create(data: Partial<WiedervorlageEintrag>) {
		try {
			const response = await wiedervorlageState.addWiedervorlage(data);
			await wiedervorlageState.ladeWiedervorlagen();
			emit("created", response);
		} finally {
			closeModal();
		}
	}

	async function update(data: Partial<WiedervorlageEintrag>) {
		// add update in further implementation steps
	}

	watch(
		show,
		async () => {
			if (show.value === true) {
				// reset data to inital data
				resetModal();
			}
		}
	);

	watch(
		() => [props.personId],
		() => {
			modelProxy.proxy.idPerson = props.personId ?? null;
		}
	);

	watch(
		() => [props.type],
		() => {
			modelProxy.proxy.typPerson = getTypPerson();
		}
	);
</script>
