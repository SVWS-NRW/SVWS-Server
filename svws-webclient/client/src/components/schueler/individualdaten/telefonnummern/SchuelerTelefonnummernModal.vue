<template>
	<svws-ui-modal :show @update:show="emit('closeModal')" size="small">
		<template #modalTitle>{{ telefonnummernModalTitle }}</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="1" class="text-left">
				<ui-select label="Telefonart"
					v-model="data.telefonart.value"
					:manager="telefonartenSelectManager"
					:validation="() => data.getFehler('idTelefonArt')"
					required :readonly />
				<svws-ui-text-input placeholder="Telefonnummer"
					v-model="data.proxy.telefonnummer"
					type="tel"
					:validation="() => data.getFehler('telefonnummer')"
					:max-len="20"
					:readonly required />
				<svws-ui-tooltip class="col-span-full">
					<svws-ui-text-input placeholder="Bemerkung"
						v-model="data.proxy.bemerkung"
						type="text"
						:validation="() => data.getFehler('bemerkung')"
						:max-len="50"
						:readonly />
					<template #content>
						{{ data.proxy.bemerkung ?? 'Bemerkung' }}
					</template>
				</svws-ui-tooltip>
				<svws-ui-spacing />
				<svws-ui-checkbox v-model="data.proxy.istGesperrt" class="col-span-full" :readonly>
					Für Weitergabe gesperrt
				</svws-ui-checkbox>
			</svws-ui-input-wrapper>
			<svws-ui-notification v-if="keineTelefonartenVorhanden" type="warning">
				Die Liste der Telefonarten ist leer, es sollte mindestens eine Telefonart unter
				Schule/Kataloge angelegt werden, damit zusätzliche Telefonnummern eine gültige Zuordnung haben.
			</svws-ui-notification>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="emit('closeModal')">Abbrechen</svws-ui-button>
				<svws-ui-button @click="emit('save')" :disabled="saveTelefonnummernDisabled">
					Speichern
				</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import type { Telefonart } from "@core/core/data/schule/Telefonart";
	import { useTelefonartenState } from "@ui/states/kataloge/TelefonartenState";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import type { SchuelerTelefonnummerModelProxy } from "../modelproxy/SchuelerTelefonnummerModelProxy";

	const props = defineProps<{
		data: SchuelerTelefonnummerModelProxy;
		readonly: boolean;
		show: boolean;
		isPatch: boolean;
	}>();

	const emit = defineEmits<{
		'closeModal': [];
		'save': [];
	}>();

	const telefonartenState = useTelefonartenState();

	const telefonnummernModalTitle = computed(() =>
		(props.isPatch) ? "Telefonnummer bearbeiten" : "Telefonnummer hinzufügen"
	);

	const keineTelefonartenVorhanden = computed<boolean>(() =>
		telefonartenState.telefonarten.list.size() === 0
	);

	const saveTelefonnummernDisabled = computed<boolean>(() =>
		props.data.hatBlockierendeFehler() || keineTelefonartenVorhanden.value
	);

	const telefonartenSelectManager = new SelectManager<Telefonart>({
		options: computed(() => telefonartenState.telefonarten.list),
		optionDisplayText: getTelefonartSelectText,
		selectionDisplayText: getTelefonartSelectText,
	});

	function getTelefonartSelectText(telefonart: Telefonart): string {
		return telefonart.bezeichnung;
	}

</script>
