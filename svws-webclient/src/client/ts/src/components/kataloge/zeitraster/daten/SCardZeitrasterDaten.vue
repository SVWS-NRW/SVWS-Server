<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<svws-ui-multi-select :model-value="Wochentag.fromIDorException(data.wochentag)" @update:model-value="doPatch({wochentag: Number($event.id)})" :items="Wochentag.values()" :item-text="i=>i.beschreibung" />
			<SvwsUiTextInput type="number" :model-value="data.unterrichtstunde" @update:model-value="doPatch({unterrichtstunde: Number($event)})" />
			<SvwsUiTextInput type="number" :model-value="data.stundenbeginn" @update:model-value="doPatch({stundenbeginn: Number($event)})" />
			<SvwsUiTextInput type="number" :model-value="data.stundenende" @update:model-value="doPatch({stundenende: Number($event)})" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanZeitraster} from "@svws-nrw/svws-core";
	import { Wochentag } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: StundenplanZeitraster
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<StundenplanZeitraster>): void;
	}>()

	function doPatch(data: Partial<StundenplanZeitraster>) {
		emit('patch', data);
	}

</script>
