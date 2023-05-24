<template>
	<svws-ui-content-card title="Grunddaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Bezeichnung" :model-value="data().bezeichnungStundenplan"
					@update:model-value="doPatch({ bezeichnungStundenplan: String($event) })" type="text" />
				<svws-ui-text-input placeholder="Gültig ab" :model-value="data().gueltigAb"
					@update:model-value="doPatch({ gueltigAb: String($event) })" type="date" />
				<svws-ui-text-input placeholder="Gültig bis" :model-value="data().gueltigBis"
					@update:model-value="doPatch({ gueltigBis: String($event) })" type="date" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { Stundenplan } from "@svws-nrw/svws-core";

	const props = defineProps<{
		data: () => Stundenplan;
	}>();

	const emit = defineEmits<{
		// (e: 'patch', data: Partial<Stundenplan>): void;
		patch: [data: Partial<Stundenplan>];
	}>()

	function doPatch(data: Partial<Stundenplan>) {
		emit('patch', data);
	}

</script>
