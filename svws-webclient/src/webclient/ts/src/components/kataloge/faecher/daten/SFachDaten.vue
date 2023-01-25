<template>
	<div v-if="visible" class="app-container">
		<s-card-fach-basisdaten :item="item" :data="data" />
	</div>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataFach } from "~/apps/kataloge/faecher/DataFach";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeFaecherDaten } from "~/router/apps/faecher/RouteKatalogFaecherDaten";

	const props = defineProps<{
		item: ShallowRef<FaecherListeEintrag | undefined>;
		data: DataFach;
		schule: DataSchuleStammdaten;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return (!routeFaecherDaten.hidden()) && (props.item.value !== undefined);
	});

</script>
