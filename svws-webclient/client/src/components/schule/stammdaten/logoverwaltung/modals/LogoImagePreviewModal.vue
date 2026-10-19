<template>
	<svws-ui-modal show size="big" @update:show="onUpdateShow">
		<template #modalTitle>
			{{ logoModel.proxy.bezeichnung }}
		</template>
		<template #modalContent>
			<div class="w-full shrink-0 @container">
				<logo-image mode="tooltip"
					:logo-base64="logoModel.proxy.base64"
					class="mx-auto"
					:style="getCssLogoSize(logoModel.proxy.kennung, '100cqw', `${LOGO_PREVIEW_MAX_HEIGHT_REM}rem`)"
					:alt="`Große Vorschau des Bildes für das Logo '${logoModel.proxy.bezeichnung}'`" />
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import LogoImage from "../LogoImage.vue";
	import { getCssLogoSize } from "../LogoUtils";
	import type { LogoModelProxy } from "../modelProxy/LogoModelProxy";

	const LOGO_PREVIEW_MAX_HEIGHT_REM = 50;

	const props = defineProps<{
		logoModel: LogoModelProxy;
	}>();

	const emit = defineEmits<{
		(e: "close"): void;
	}>();


	function onUpdateShow(value: boolean): void {
		if (!value) {
			emit("close");
		}
	}

</script>
