<template>
	<svws-ui-content-card title="Allgemein">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-multi-select :model-value="Wochentag.fromIDorException(data.wochentag)" @update:model-value="doPatch({wochentag: Number($event.id)})" :items="Wochentag.values()" :item-text="i=>i.beschreibung" />
			<svws-ui-text-input type="number" :model-value="data.unterrichtstunde" @update:model-value="doPatch({unterrichtstunde: Number($event)})" />
			<svws-ui-text-input type="number" :model-value="data.stundenbeginn" @update:model-value="doPatch({stundenbeginn: Number($event)})" />
			<svws-ui-text-input type="number" :model-value="data.stundenende" @update:model-value="doPatch({stundenende: Number($event)})" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { StundenplanZeitraster} from "@core";
	import { Wochentag } from "@core";

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
