<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" v-model="inputBezeichnung" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung in Statistik" v-model="inputFachStatistik" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ShallowRef, WritableComputedRef } from "vue";
	import { DataFach } from "~/apps/kataloge/faecher/DataFach";

	const props = defineProps<{
		item: ShallowRef<FaecherListeEintrag | undefined>;
		data: DataFach;
	}>();

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel ?? undefined,
		set: (value) => void props.data.patch({ kuerzel: value })
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.bezeichnung ?? undefined,
		set: (value) => void props.data.patch({ bezeichnung: value })
	});

	const inputFachStatistik: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzelStatistik ?? undefined,
		set: (value) => void props.data.patch({ kuerzelStatistik: value })
	});

</script>
