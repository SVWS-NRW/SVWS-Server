<template>
	<div class="w-full min-h-8 flex items-center grow">
		<div v-if="attributeValue !== undefined" @mouseover="pendingIndicatorHovered = true" @mouseleave="pendingIndicatorHovered = false"
			ref="gruppenProzessIconContainer" @click="revert" class="h-auto w-auto me-2" :id="`tooltip-${String(attributeName)}`">
			<SvwsUiTooltip color="primary" :show-arrow="false" :indicator="false">
				<template #content>
					<div class="py-1 px-1 flex flex-col">
						{{ tooltipTextActual }}
						<div class="w-auto bg-white h-0.5 my-2" />
						{{ tooltipTextAfter }}
					</div>
				</template>
				<div class="h-full min-h-8 w-8 px-1 bg-ui-brand hover:bg-ui-brand-hover rounded content-center center align-middle cursor-pointer">
					<span v-if="pendingIndicatorHovered" class="inline-block i-ri-close-line icon-lg h-full min-h-8 w-full align-middle bg-white" />
					<span v-else class="inline-block i-ri-information-line icon-lg h-full min-h-8 w-full align-middle bg-white" />
				</div>
			</SvwsUiTooltip>
		</div>

		<div ref="container" class="grow grid items-center">
			<slot />
		</div>

		<div class="pt-3 ms-2 me-4 w-auto h-full flex justify-center" :id="`Clearbutton-${String(attributeName)}`">
			<SvwsUiTooltip v-if="nullable" :show-arrow="false" :indicator="false">
				<template #content>
					Aktuellen Wert löschen
				</template>
				<span class="icon-lg i-ri-delete-bin-line" @click="setNull"
					:class="{ 'icon-ui-danger cursor-pointer': showClearButton, 'icon-ui-disabled': !showClearButton }" />
			</SvwsUiTooltip>
			<div v-else class="min-w-6" />
		</div>
	</div>
</template>

<script setup lang='ts' generic="T">

	import { computed, ref } from 'vue';

	import type { MultiModelProxy } from '../../model/MultiModelProxy';

	const props = withDefaults(defineProps<{
		model: MultiModelProxy<any>,
		attributeName: keyof T,
		nullable?: boolean,
	}>(), {
		nullable: true,
	});

	const container = ref<HTMLElement | null>(null);
	const pendingIndicatorHovered = ref<boolean>(false);

	// Die aktuellen Werte mit der Anzahl, wie häufig dieser Wert gesetzt ist (null für fehlende Werte)
	const actualValuesWithAmount = computed(() => props.model.valueCounts(props.attributeName));

	const attributeValue = computed(() => {
		// Prüfe, ob für das Attribut ein Pending vorliegt
		return props.model.isPending(props.attributeName) ? props.model.pending[props.attributeName] : undefined;
	});

	const pendingAmountOverall = computed<number>(() => props.model.count);

	const showClearButton = computed<boolean>(() => {
		for (const key of actualValuesWithAmount.value.keys()) {
			if ((key !== null) && (attributeValue.value !== null)) {
				return true;
			}
		}
		return false;
	});

	const tooltipTextActual = computed<string>(() => {
		let tooltipActual = 'Aktuell: ';
		for (const [key, amount] of actualValuesWithAmount.value) {
			tooltipActual += (key === null) ? 'Keine Daten' : props.model.displayText(props.attributeName, key);
			tooltipActual += ` (${amount}), `;
		}
		const tailCut = tooltipActual.slice(0, -2);
		return tailCut.length > 200 ? `${tailCut.slice(0, 200)} ...` : tailCut;
	});

	const tooltipTextAfter = computed(() => {
		let tooltipAfter = 'Nachher: ';
		if ((attributeValue.value !== undefined) && (attributeValue.value !== null)) {
			tooltipAfter += props.model.displayText(props.attributeName, attributeValue.value);
		} else {
			tooltipAfter += 'Keine Daten';
		}

		tooltipAfter += ` (${pendingAmountOverall.value})`;
		return tooltipAfter;
	});

	// Entfernt den aktuellen pendingState für das Attribut
	function revert() {
		props.model.removePending(props.attributeName);
	}

	// Setzt den pendingState für das Attribut auf <code>null</code>
	function setNull() {
		if (!props.nullable || !showClearButton.value) {
			return;
		}
		props.model.setPending(props.attributeName, null);
	}

</script>
