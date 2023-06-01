<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select :model-value="Wochentag.fromIDorException(data.wochentag)" @update:model-value="doPatch({wochentag: Number($event.id)})" :items="Wochentag.values()" :item-text="i=>i.beschreibung" />
			<SvwsUiTextInput type="number" :model-value="data.beginn" @update:model-value="doPatch({beginn: Number($event)})" />
			<SvwsUiTextInput type="number" :model-value="data.ende" @update:model-value="doPatch({ende: Number($event)})" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanPausenzeit } from "@svws-nrw/svws-core";
	import { Wochentag } from "@svws-nrw/svws-core";


	const props = defineProps<{
		data: StundenplanPausenzeit
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<StundenplanPausenzeit>): void;
	}>()

	function doPatch(data: Partial<StundenplanPausenzeit>) {
		emit('patch', data);
	}

</script>
