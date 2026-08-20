<template>
	<div v-if="logoBase64 === ''" class="flex h-full w-full flex-col items-center justify-center gap-1">
		<svws-ui-tooltip v-if="mode === 'tooltip'" position="top">
			<span class="icon-xl i-ri-file-close-line icon-ui-caution" />
			<template #content>
				Kein Bild vorhanden
			</template>
		</svws-ui-tooltip>
		<template v-else>
			<span class="icon-xl i-ri-file-close-line icon-ui-caution" />
			<span>Kein Bild vorhanden</span>
		</template>
	</div>
	<button v-else-if="!imgError && clickable"
		type="button"
		class="h-full w-full rounded-md p-0.5 hover:ring-2 focus:ring-2 focus:outline-hidden ring-ui"
		:aria-label="ariaLabel ?? alt"
		@click="emit('click')">
		<img :src="logoBase64"
			:alt
			class="h-full w-full object-fill"
			@error="onImgError">
	</button>
	<img v-else-if="!imgError"
		:src="logoBase64"
		:alt
		class="h-full w-full object-fill"
		@error="onImgError">
	<div v-else class="flex h-full w-full flex-col items-center justify-center gap-1">
		<svws-ui-tooltip v-if="mode === 'tooltip'" position="top">
			<span class="icon-xl i-ri-eye-off-line icon-ui-warning" />
			<template #content>
				Keine Vorschau möglich
			</template>
		</svws-ui-tooltip>
		<template v-else>
			<span class="icon-xl i-ri-eye-off-line icon-ui-warning" />
			<span>Keine Vorschau möglich</span>
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ref, watch } from "vue";

	const props = withDefaults(defineProps<{
		logoBase64: string;
		alt: string;
		ariaLabel?: string;
		mode?: "tooltip" | "inline";
		clickable?: boolean;
	}>(), {
		mode: "tooltip",
		clickable: false,
		ariaLabel: undefined,
	});

	const emit = defineEmits<{
		(e: "click" | "error"): void;
	}>();

	const imgError = ref(false);

	watch(() => props.logoBase64, () => {
		imgError.value = false;
	});

	function onImgError(): void {
		imgError.value = true;
		emit("error");
	}

</script>
