<template>
	<!-- Optionaler Slot für einen Button, der das Modal öffnet -->
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
						<template v-else-if="data.namePerson">
							<span v-if="icon !== null" :class="`icon ${icon} mr-2`" />
							<span>{{ data.namePerson }}</span>
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
	import { computed, shallowRef, watch } from 'vue';
	import { dateTodayPlus, formatDateToDateTime, getDateFromDateTime } from "~/utils/date";
	import type { Wiedervorlage } from "~/components/wiedervorlage/Wiedervorlage";
	import { WiedervorlageModelProxy } from "~/components/wiedervorlage/WiedervorlageModelProxy";
	import type { BenutzergruppeListeEintrag } from '@core/core/data/benutzer/BenutzergruppeListeEintrag';
	import type { WiedervorlageEintrag } from '@core/core/data/schule/WiedervorlageEintrag';
	import { useNotificationsState } from '@ui/states/NotificationsState';
	import { useWiedervorlageState } from '@ui/states/WiedervorlageState';

	const props = withDefaults(defineProps<{
		mode?: "create" | "edit",
		type?: "allgemein" | "schueler" | "lehrkraft" | "erzieher",
		data: Partial<WiedervorlageEintrag>
	}>(), {
		mode: "create",
		type: "allgemein",
	});

	const wiedervorlageState = useWiedervorlageState();
	const notificationsState = useNotificationsState();

	const emit = defineEmits<{
		// Event nach Anlegen eines neuen Eintrages
		created: [val: WiedervorlageEintrag];
		// Event nach Änderung eines Eintrags
		updated: [];
	}>();

	const show = defineModel({ type: Boolean, default: false });

	const modelProxy = shallowRef<WiedervorlageModelProxy>(new WiedervorlageModelProxy(() => getInitialData())) ;

	const hatFehler = computed(() => {
		return modelProxy.value.getAlleFehler().size() > 0;
	});

	const icon = computed<string | null>(() => {
		if (props.type === "schueler" || props.type === "erzieher") {
			return "i-ri-group-line";
		}
		if (props.type === "lehrkraft") {
			return "i-ri-briefcase-line";
		}
		return null;
	});

	watch(
		show,
		() => {
			if (show.value === true) {
				modelProxy.value = new WiedervorlageModelProxy(() => getInitialData());
			}
		}
	);

	function getTypPerson() {
		const typPersonMap: Record<string, 1 | 2 | 3> = {
			lehrkraft: 1,
			schueler: 2,
			erzieher: 3,
		};

		return typPersonMap[props.type] ?? null;
	}

	function getInitialDate(): string | null {
		switch (props.mode) {
			case "create":
				return dateTodayPlus({ days: 7 });
			case "edit":
			default:
				if (props.data.tsWiedervorlage !== null && props.data.tsWiedervorlage !== undefined) {
					return getDateFromDateTime(props.data.tsWiedervorlage) ?? null;
				}
				return null;
		}
	}

	function findBenutzergruppeById(
		id: number
	): BenutzergruppeListeEintrag | null {
		for (const gruppe of wiedervorlageState.benutzerGruppen) {
			if (gruppe.id === id) {
				return gruppe;
			}
		}

		return null;
	}


	function getInitialData(): Wiedervorlage {
		const idBenutzergruppe = props.data.idBenutzergruppe !== null && props.data.idBenutzergruppe !== undefined ?
			findBenutzergruppeById(props.data.idBenutzergruppe) : null;

		const tsWiedervorlage = getInitialDate();

		return {
			id: props.data.id ?? null,
			idBenutzergruppe,
			typPerson: getTypPerson(),
			idPerson: props.data.idPerson ?? null,
			bemerkung: props.data.bemerkung ?? "",
			tsWiedervorlage,
			automatischErledigt: props.data.automatischErledigt ?? false,
		};
	}

	function openModal() {
		show.value = true;
	}

	function closeModal() {
		show.value = false;
	}

	async function submit() {
		const { id, ...rest } = { ...modelProxy.value.proxy };

		const submitData: Partial<WiedervorlageEintrag> = {
			...rest,
			tsWiedervorlage: modelProxy.value.proxy.tsWiedervorlage === null ? null : (formatDateToDateTime(modelProxy.value.proxy.tsWiedervorlage) ?? null),
			idBenutzergruppe: modelProxy.value.proxy.idBenutzergruppe?.id ?? null,
		};

		if (props.mode === "create") {
			await create(submitData);
		} else if (id !== null) {
			await update(submitData, id);
		}
	}

	async function create(data: Partial<WiedervorlageEintrag>) {
		const response = await wiedervorlageState.addWiedervorlage(data);
		emit("created", response);
		notificationsState.success("Gespeichert", "Die Wiedervorlage wurde erstellt.");
		closeModal();
	}

	async function update(data: Partial<WiedervorlageEintrag>, id: number) {
		await wiedervorlageState.patchWiedervorlage(data, id);
		emit("updated");
		notificationsState.success("Gespeichert", "Die Wiedervorlage wurde angepasst.");

		closeModal();
	}

</script>
