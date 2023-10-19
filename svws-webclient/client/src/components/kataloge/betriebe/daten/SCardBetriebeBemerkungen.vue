<template>
	<svws-ui-input-wrapper :grid="2" class="input-wrapper--checkboxes">
		<svws-ui-checkbox :model-value="daten.ausbildungsbetrieb" @update:model-value="ausbildungsbetrieb => doPatch({ ausbildungsbetrieb } )"> Ausbildungsbetrieb </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.bietetPraktika" @update:model-value="bietetPraktika => doPatch({ bietetPraktika })"> Bietet Praktikumsplätze </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.Massnahmentraeger" @update:model-value="Massnahmentraeger => doPatch({ Massnahmentraeger })"> Maßnahmenträger </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.ErwFuehrungszeugnis" @update:model-value="ErwFuehrungszeugnis => doPatch({ ErwFuehrungszeugnis })"> Erweitertes Führungszeugnis notwendig </svws-ui-checkbox>
		<svws-ui-checkbox :model-value="daten.BelehrungISG" @update:model-value="BelehrungISG => doPatch({ BelehrungISG })"> Belehrung n. Infektionsschutzgesetz notwendig </svws-ui-checkbox>
	</svws-ui-input-wrapper>
	<svws-ui-spacing />
	<svws-ui-spacing />
	<svws-ui-textarea-input :model-value="daten.bemerkungen" @change="bemerkungen => doPatch({ bemerkungen: bemerkungen || '' })" placeholder="Bemerkungen" />
</template>

<script setup lang="ts">
	import type { BetriebStammdaten } from "@core";

	const props = defineProps<{
		daten: BetriebStammdaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<BetriebStammdaten>): void;
	}>()

	function doPatch(data: Partial<BetriebStammdaten>) {
		emit('patch', data);
	}

</script>
