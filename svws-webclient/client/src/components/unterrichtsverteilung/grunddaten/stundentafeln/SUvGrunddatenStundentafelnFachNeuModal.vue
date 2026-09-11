<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden" size="small">
		<template #modalTitle>Fach zur Stundentafel hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-select v-model="fach" :items="listFaecher" :item-text="getFachBezeichnung" placeholder="Fach" class="col-span-2" />
				<svws-ui-input-number v-model="wochenstunden" placeholder="Wochenstunden" :min="0" :step="1" />
				<svws-ui-input-number v-model="ergaenzungsstunden" placeholder="davon Ergänzungsstunden" :min="0" :max="wochenstunden" :step="1" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="hinzufuegen" :disabled="!isValid">Hinzufügen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";

	import type { UvFach } from "@core/core/data/uv/UvFach";
	import type { UvStundentafel } from "@core/core/data/uv/UvStundentafel";
	import { useUvState } from "@ui/states/UvState";

	const props = defineProps<{
		stundentafel: UvStundentafel;
		abschnitt: number;
	}>();
	const state = useUvState();

	const show = ref<boolean>(false);
	const fach = ref<UvFach | null>(null);
	const wochenstunden = ref(3);
	const ergaenzungsstunden = ref(0);

	const listFaecher = computed(() => [...state.uvManager.fachGetMengeGueltigUndFehlendByStundentafel(props.stundentafel, props.abschnitt)]);

	function getFachBezeichnung(f: UvFach): string {
		const fachdaten = state.uvManager.fachdatenGetByFach(f);
		return fachdaten.bezeichnung;
	}

	const isValid = computed(() => fach.value !== null && wochenstunden.value > 0);

	const openModal = () => {
		fach.value = null;
		wochenstunden.value = 3;
		ergaenzungsstunden.value = 0;
		show.value = true;
	};

	async function hinzufuegen() {
		if (!isValid.value || !fach.value) {
			return;
		}
		await state.addStundentafelFach({
			idStundentafel: props.stundentafel.id,
			abschnitt: props.abschnitt,
			idFach: fach.value.id,
			wochenstunden: wochenstunden.value,
			davonErgaenzungsstunden: ergaenzungsstunden.value,
		});
		show.value = false;
	}
</script>
