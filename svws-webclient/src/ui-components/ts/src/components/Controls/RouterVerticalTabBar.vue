<template>
    <div class="router-vertical-tab-bar--area">
        <div class="router-vertical-tab-bar--wrapper print:hidden">
            <div v-if="state.scrolled" class="router-vertical-tab-bar--scroll-button-background router-vertical-tab-bar--scroll-button-background-up">
                <button class="router-vertical-tab-bar--scroll-button" @click="scroll('up')">
                   <Icon> <i-ri-arrow-up-line /> </Icon>
                </button>
            </div>
            <div ref="contentEl" class="router-vertical-tab-bar--content">
                <router-tab-bar-button v-for="(route, index) in props.routes" :route="route" :selected="selected" 
                    :hidden="isHidden(index)" @select="select(route)" />
            </div>
            <div v-if="!state.scrolledMax"
                class="router-vertical-tab-bar--scroll-button-background router-vertical-tab-bar--scroll-button-background-down">
                <button class="router-vertical-tab-bar--scroll-button" @click="scroll('down')">
                    <Icon> <i-ri-arrow-down-line /> </Icon>
                </button>
            </div>
        </div>
        <div class="router-vertical-tab-bar--panel">
            <slot />
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { computed, onMounted, onUnmounted, onUpdated, ref, WritableComputedRef } from 'vue';
    import { RouteRecordRaw } from "vue-router";

    const props = defineProps<{
        routes: RouteRecordRaw[]
        hidden: boolean[] | undefined
        modelValue: RouteRecordRaw
    }>();

    const emit = defineEmits<{ (e: 'update:modelValue', value: RouteRecordRaw): void, }>();

    type ComponentData = {
        scrolled: boolean;
        scrolledMax: boolean;
        scrollFactor: number;
        maxScrollTop: number;
    }

    const contentEl = ref();
    const selected: WritableComputedRef<RouteRecordRaw> = computed({
        get() : RouteRecordRaw {
            return props.modelValue;
        },
        set(value: RouteRecordRaw ) {
            emit('update:modelValue', value);
        }
    });

    function isHidden(index: number) {
        if ((props.hidden === undefined) || props.hidden[index] === undefined)
            return false;
        return props.hidden[index];
    }

    const state = ref<ComponentData>({
        scrolled: false,
        scrolledMax: false,
        scrollFactor: 4,
        maxScrollTop: 0,
    });

    onMounted(() => {
        state.value.maxScrollTop = (contentEl.value?.scrollHeight ?? 0) - (contentEl.value?.clientHeight ?? 0);
        state.value.scrolledMax = (contentEl.value?.scrollTop ?? 0) >= state.value.maxScrollTop;
        contentEl.value?.addEventListener("scroll", handleScroll);
        window.addEventListener("resize", handleScroll);
    })


    onUnmounted(() => {
        contentEl.value?.removeEventListener("scroll", handleScroll);
        window.removeEventListener("resize", handleScroll);
    });


    onUpdated(() => {
        handleScroll();
    });


    function handleScroll() {
        state.value.scrolled = (contentEl.value?.scrollTop ?? 0) > 0;
        state.value.maxScrollTop =
            (contentEl.value?.scrollHEight ?? 0) - (contentEl.value?.clientHeight ?? 0);
        state.value.scrolledMax = (contentEl.value?.scrollTop ?? 0) >= state.value.maxScrollTop;
    }

    function scroll(direction: 'up' | 'down') {
        const dir = direction == "up" ? -1 : 1;
        contentEl.value?.scrollBy({
            left: 0,
            top: (dir * contentEl.value.scrollHeight) / state.value.scrollFactor,
            behavior: "smooth"
        });
    }

    function select(route: RouteRecordRaw) {
        selected.value = route;
    }

</script>


<style lang="postcss">
    .router-vertical-tab-bar--area {
        @apply flex flex-row flex-grow items-start;
		@apply w-full;
    }

    .router-vertical-tab-bar--panel {
        @apply flex-grow overflow-y-auto;
    }

    .router-vertical-tab-bar--wrapper {
		@apply flex flex-col flex-shrink items-start;
		@apply overflow-hidden;
		@apply relative;
		@apply rounded-md;
		@apply mr-4;
    }

    .router-vertical-tab-bar--content {
		@apply bg-light;
		@apply flex flex-col items-center;
		@apply overflow-y-scroll;
		@apply relative;
		@apply rounded-md;
		@apply space-y-2 p-1;

		-ms-overflow-style: none;
		/* Remove Scrollbar in IE and Edge */
		scrollbar-width: none;
		/* Remove Scrollbar in Firefox */
    }

    .router-vertical-tab-bar--content::-webkit-scrollbar {
        display: none;
        /* Remove Scrollbar in Chromium basesd Browsers */
    }

    .router-vertical-tab-bar--scroll-button-background {
		@apply absolute z-20;
		@apply w-full;
		@apply pointer-events-none;
		@apply from-transparent via-light to-light;
    }

    .router-vertical-tab-bar--scroll-button-background-down {
		@apply bg-gradient-to-b;
		@apply pt-8;
		@apply rounded-b-full;
		bottom: 0.875rem;
    }

    .router-vertical-tab-bar--scroll-button-background-up {
		@apply bg-gradient-to-t;
		@apply pb-8;
		@apply rounded-t-full;
		top: 0.875rem;
    }

    .router-vertical-tab-bar--scroll-button {
		@apply w-full;
		@apply inline-flex items-center justify-center;
		@apply pointer-events-auto;
		@apply py-3.5;
		@apply rounded-full;
		@apply text-black;
    }

    .router-vertical-tab-bar--scroll-button:focus {
		@apply outline-none ring ring-inset ring-primary ring-opacity-75;
    }

</style>
