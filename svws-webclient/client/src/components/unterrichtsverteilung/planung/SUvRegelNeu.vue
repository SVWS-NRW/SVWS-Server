<template>
	<section class="flex min-h-0 flex-1 flex-col bg-ui">
		<div class="flex shrink-0 items-center justify-between gap-2 border-b border-ui-10 p-3">
			<h3 class="font-semibold">Regel erstellen</h3>
			<svws-ui-button type="secondary" :disabled="pending" @click="props.abbrechen()">Abbrechen</svws-ui-button>
		</div>
		<div class="flex min-h-0 flex-1 flex-col gap-3 overflow-auto p-3">
			<svws-ui-content-card title="Blockungsregel">
				<s-uv-regel-formular v-model:entwurf="entwurf" v-model:werte="werte" v-model:listenwerte="listenwerte" :pending />
			</svws-ui-content-card>
			<svws-ui-notification v-if="fehler" type="error">{{ fehler }}</svws-ui-notification>
		</div>
		<div class="flex shrink-0 justify-end border-t border-ui-10 p-3">
			<svws-ui-button type="primary"
				:disabled="pending || definition === undefined"
				:is-loading="pending"
				@click="speichern">
				Regel erstellen
			</svws-ui-button>
		</div>
	</section>
</template>

<script setup lang="ts">
	import { computed, ref } from 'vue';

	import { UvBlockungRegel } from '@core/core/data/uv/regel/UvBlockungRegel';
	import { useUvState } from '@ui/states/UvState';

	import SUvRegelFormular from './SUvRegelFormular.vue';
	import type { UvPlanungsManager } from './UvPlanungsManager';
	import { uvRegelDefinitionen } from './UvRegelFelder';

	const props = defineProps<{
		auswahlManager: UvPlanungsManager | null;
		abbrechen: () => void;
	}>();
	const emit = defineEmits<{
		created: [id: number];
	}>();
	const state = useUvState();
	const entwurf = ref(new UvBlockungRegel());
	const werte = ref<(number | null)[]>([]);
	const listenwerte = ref<number[]>([]);
	const pending = ref(false);
	const fehler = ref('');
	const definitionen = uvRegelDefinitionen;
	const definition = computed(() => definitionen.find(item => item.typ.nr === entwurf.value.typ));

	async function speichern(): Promise<void> {
		const manager = props.auswahlManager;
		if (pending.value || manager === null) {
			return;
		}
		try {
			const regel = manager.regelEntwurf(entwurf.value, werte.value, listenwerte.value);
			pending.value = true;
			const neu = await state.regeln.create(regel);
			emit('created', neu.id);
		} catch (error) {
			fehler.value = error instanceof Error ? error.message : 'Die Regel konnte nicht erstellt werden.';
		} finally {
			pending.value = false;
		}
	}
</script>
