<template>
	<svws-ui-content-card title="Bemerkungen">
		<div class="entry-content">
			<div class="col-span-2">
				<svws-ui-textarea-input :model-value="daten.bemerkungen" @change="bemerkungen => doPatch({ bemerkungen: bemerkungen || '' })"
					placeholder="Bemerkungen" />
			</div>
			<svws-ui-checkbox v-model="ausbildungsbetrieb"> Ausbildungsbetrieb </svws-ui-checkbox>
			<svws-ui-checkbox v-model="bietetPraktika"> Bietet Praktumsplätze </svws-ui-checkbox>
			<svws-ui-checkbox v-model="Massnahmentraeger"> Maßnahmenträger </svws-ui-checkbox>
			<svws-ui-checkbox v-model="ErwFuehrungszeugnis"> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
			<svws-ui-checkbox v-model="BelehrungISG"> Belehrung n. Infektionsschutzgesetz notwendig </svws-ui-checkbox>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { BetriebStammdaten } from "@core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		daten: BetriebStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
		emit('patch', data);
	}

	const ausbildungsbetrieb: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.daten.ausbildungsbetrieb ?? undefined,
		set: (value) => void doPatch({ ausbildungsbetrieb: value } )
	});

	const bietetPraktika: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.daten.bietetPraktika ?? undefined,
		set: (value) => void doPatch({ bietetPraktika: value } )
	});

	const Massnahmentraeger: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.daten.Massnahmentraeger ?? undefined,
		set: (value) => void doPatch({ Massnahmentraeger: value } )
	});

	const ErwFuehrungszeugnis: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.daten.ErwFuehrungszeugnis ?? undefined,
		set: (value) => void doPatch({ ErwFuehrungszeugnis: value } )
	});

	const BelehrungISG: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.daten.BelehrungISG ?? undefined,
		set: (value) => void doPatch({ BelehrungISG: value } )
	});
</script>
