<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>Eingabe des Datums und der Uhrzeit</template>
		<template #modalContent>
			<div class="flex w-fit">
				<svws-ui-text-input type="datetime-local" :placeholder="`Eingabe ${props.modus === 'ab' ? 'von' : 'bis'}`"
					:model-value="model" class="min-w-64"
					@change="update" :disabled removable />
				<div class="content-end mb-1">
					<svws-ui-button type="secondary" @click="setToNow" :disabled class=""> Aktuelle&nbsp;Zeit </svws-ui-button>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false" :disabled> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="update()" :disabled> OK </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref, watchEffect } from 'vue';
	import type { NotenmodulConfigManagerSperrungen, NotenmodulConfigManagerSperrungenZeile } from '~/router/apps/notenmodul/NotenmodulConfigManagerSperrungen';

	const props = defineProps<{
		manager: () => NotenmodulConfigManagerSperrungen;
		row: () => NotenmodulConfigManagerSperrungenZeile;
		/** gibt an, ob als Modus der Beginn oder das Ende der Sperrzeit verwendet werden soll */
		modus: 'ab' | 'bis';
	}>();

	const show = ref<boolean>(false);
	const disabled = ref<boolean>(false);
	const model = ref<string | null>(null);

	watchEffect(() => {
		if (props.modus === 'ab') {
			model.value = props.row().tsEingabeAb;
		} else {
			model.value = props.row().tsEingabeBis;
		}
	});

	async function update(datum?: string | null) {
		if (datum !== undefined) {
			model.value = datum;
		}
		if ((props.modus === 'ab' && props.row().tsEingabeAb !== model.value) || (props.modus === 'bis' && props.row().tsEingabeBis !== model.value)) {
			await props.manager().setzeDatumNoteneingabe(props.row(), model.value, props.modus === 'ab');
		}
		show.value = false;
		disabled.value = false;
	}

	function setToNow() {
		const utc = new Date();
		const localDate = new Date(utc.getTime() - (utc.getTimezoneOffset() * 60000));
		model.value = localDate.toISOString().slice(0, -8);
	}

	function openModal() {
		show.value = true;
	}


</script>
