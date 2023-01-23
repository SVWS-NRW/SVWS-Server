<template>
	<svws-ui-content-card class="lg:col-span-2 4xl:col-span-3 mt-auto pt-8">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-textarea-input placeholder="Bemerkungen" v-model="inputBemerkungen" resizeable="vertical" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";

	const props = defineProps<{ stammdaten: DataSchuelerStammdaten }>();

	const daten: ComputedRef<SchuelerStammdaten> = computed(() => props.stammdaten.daten || new SchuelerStammdaten());

	const inputBemerkungen: WritableComputedRef<string | undefined> = computed({
		get: () => daten.value.bemerkungen?.toString() || undefined,
		set: (value) => void props.stammdaten.patch({ bemerkungen: value })
	});

</script>
